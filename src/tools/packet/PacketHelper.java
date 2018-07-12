package tools.packet;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleCoolDownValueHolder;
import client.MapleFriendship.MapleFriendshipType;
import client.MapleQuestStatus;
import client.MapleTrait.MapleTraitType;
import client.Skill;
import client.SkillEntry;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.MapleRing;
import constants.GameConstants;
import handling.Buffstat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import server.MapleItemInformationProvider;
import server.MapleShop;
import server.MapleShopItem;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.AbstractPlayerStore;
import server.shops.IMaplePlayerShop;
import tools.BitTools;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class PacketHelper {
    public final static long FT_UT_OFFSET = 116444592000000000L; // EDT
    public final static long MAX_TIME = 150842304000000000L; //00 80 05 BB 46 E6 17 02
    public final static long ZERO_TIME = 94354848000000000L; //00 40 E0 FD 3B 37 4F 01
    public final static long PERMANENT = 150841440000000000L; // 00 C0 9B 90 7D E5 17 02

    public static final long getKoreanTimestamp(final long realTimestamp) {
        return getTime(realTimestamp);
    }

    public static final long getTime(long realTimestamp) {
        if (realTimestamp == -1) {
            return MAX_TIME;
        } else if (realTimestamp == -2) {
            return ZERO_TIME;
	} else if (realTimestamp == -3) {
	    return PERMANENT;
        }
        return ((realTimestamp * 10000) + FT_UT_OFFSET);
    }
	
    public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
        if (SimpleTimeZone.getDefault().inDaylightTime(new Date())) {
            timeStampinMillis -= 3600000L;
        }
        long time;
        if (roundToMinutes) {
            time = (timeStampinMillis / 1000 / 60) * 600000000;
        } else {
            time = timeStampinMillis * 10000;
        }
        return time + FT_UT_OFFSET;
    }

    public static void addQuestInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final List<MapleQuestStatus> started = chr.getStartedQuests();
	mplew.write(1); //idk
        mplew.writeShort(started.size());

        for (final MapleQuestStatus q : started) {
	    mplew.writeShort(q.getQuest().getId());
            if (q.hasMobKills()) {
                final StringBuilder sb = new StringBuilder();
                for (final int kills : q.getMobKills().values()) {
                    sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
                }
                mplew.writeMapleAsciiString(sb.toString());
            } else {
                mplew.writeMapleAsciiString(q.getCustomData() == null ? "" : q.getCustomData());
            }
        }
	mplew.writeShort(0); // size > 0 some weird string
	mplew.write(1); //dunno

        final List<MapleQuestStatus> completed = chr.getCompletedQuests();
        mplew.writeShort(completed.size());
        for (final MapleQuestStatus q : completed) {
	    mplew.writeShort(q.getQuest().getId());
            mplew.writeLong(getTime(q.getCompletionTime()));
        }
    }

    public static final void addSkillInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final Map<Skill, SkillEntry> skills = chr.getSkills();
        mplew.writeShort(skills.size());
        for (final Entry<Skill, SkillEntry> skill : skills.entrySet()) {
            mplew.writeInt(skill.getKey().getId());
            mplew.writeInt(skill.getValue().skillevel);
            addExpirationTime(mplew, skill.getValue().expiration);

            if (skill.getKey().isFourthJob()) {
                mplew.writeInt(skill.getValue().masterlevel);
            }
        }
    }

    public static final void addCoolDownInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final List<MapleCoolDownValueHolder> cd = chr.getCooldowns();
        mplew.writeShort(cd.size());
        for (final MapleCoolDownValueHolder cooling : cd) {
            mplew.writeInt(cooling.skillId);
            mplew.writeShort((int) (cooling.length + cooling.startTime - System.currentTimeMillis()) / 1000);
        }
    }

    public static final void addRocksInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final int[] mapz = chr.getRegRocks();
        for (int i = 0; i < 5; i++) { // VIP teleport map
            mplew.writeInt(mapz[i]);
        }

        final int[] map = chr.getRocks();
        for (int i = 0; i < 10; i++) { // VIP teleport map
            mplew.writeInt(map[i]);
        }

        final int[] maps = chr.getHyperRocks();
        for (int i = 0; i < 13; i++) { // VIP teleport map
            mplew.writeInt(maps[i]);
        }
        for (int i = 0; i < 13; i++) { // VIP teleport map
            mplew.writeInt(maps[i]);
        }
    }

    public static final void addRingInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeShort(0);
        //01 00 = size
        //01 00 00 00 = gametype?
        //03 00 00 00 = win
        //00 00 00 00 = tie/loss
        //01 00 00 00 = tie/loss
        //16 08 00 00 = points
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> aRing = chr.getRings(true);
        List<MapleRing> cRing = aRing.getLeft();
        mplew.writeShort(cRing.size());
        for (MapleRing ring : cRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 13);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
        }
        List<MapleRing> fRing = aRing.getMid();
        mplew.writeShort(fRing.size());
        for (MapleRing ring : fRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 13);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
        List<MapleRing> mRing = aRing.getRight();
        mplew.writeShort(mRing.size());
        int marriageId = 30000;
        for (MapleRing ring : mRing) {
            mplew.writeInt(marriageId);
            mplew.writeInt(chr.getId());
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeShort(3); //1 = engaged 3 = married
            mplew.writeInt(ring.getItemId());
            mplew.writeInt(ring.getItemId());
            mplew.writeAsciiString(chr.getName(), 13);
            mplew.writeAsciiString(ring.getPartnerName(), 13);
        }
    }

    public static void addInventoryInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeLong(chr.getMeso()); // mesos
        mplew.write(chr.getInventory(MapleInventoryType.EQUIP).getSlotLimit()); // equip slots
        mplew.write(chr.getInventory(MapleInventoryType.USE).getSlotLimit()); // use slots
        mplew.write(chr.getInventory(MapleInventoryType.SETUP).getSlotLimit()); // set-up slots
        mplew.write(chr.getInventory(MapleInventoryType.ETC).getSlotLimit()); // etc slots
        mplew.write(chr.getInventory(MapleInventoryType.CASH).getSlotLimit()); // cash slots

        final MapleQuestStatus stat = chr.getQuestNoAdd(MapleQuest.getInstance(GameConstants.PENDANT_SLOT));
        if (stat != null && stat.getCustomData() != null && Long.parseLong(stat.getCustomData()) > System.currentTimeMillis()) {
            mplew.writeLong(getTime(Long.parseLong(stat.getCustomData())));
        } else {
            mplew.writeLong(getTime(-2));
        }
        MapleInventory iv = chr.getInventory(MapleInventoryType.EQUIPPED);
        List<Item> equipped = iv.newList();
        Collections.sort(equipped);
        for (Item item : equipped) {
            if (item.getPosition() < 0 && item.getPosition() > -100) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }
        mplew.writeShort(0); // start of equipped nx
        for (Item item : equipped) {
            if (item.getPosition() <= -100 && item.getPosition() > -1000) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }

        mplew.writeShort(0); // start of equip inventory
        iv = chr.getInventory(MapleInventoryType.EQUIP);
        for (Item item : iv.list()) {
            addItemInfo(mplew, item, false, false, false, false, chr);
        }
        mplew.writeShort(0); //start of evan equips

        for (Item item : equipped) {
            if (item.getPosition() <= -1000 && item.getPosition() > -1100) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }
        mplew.writeShort(0); //start of mechanic equips, ty KDMS

        for (Item item : equipped) {
            if (item.getPosition() <= -1100 && item.getPosition() > -1200) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }
        mplew.writeShort(0); // start of android equips
        for (Item item : equipped) {
            if (item.getPosition() <= -1200) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }
        if (GameConstants.GMS) { // just for gms to use.
	    mplew.writeShort(0); // start of totem inventory
	    for (Item item : equipped) {
		if (item.getPosition() <= -5000 && item.getPosition() >= -5002) {
		    addItemInfo(mplew, item, false, false, false, false, chr);
		}
	    }
	}
        mplew.writeShort(0); // start of use inventory
        iv = chr.getInventory(MapleInventoryType.USE);
        for (Item item : iv.list()) {
            addItemInfo(mplew, item, false, false, false, false, chr);
        }
        mplew.write(0); // start of set-up inventory
        iv = chr.getInventory(MapleInventoryType.SETUP);
        for (Item item : iv.list()) {
            addItemInfo(mplew, item, false, false, false, false, chr);
        }
        mplew.write(0); // start of etc inventory
        iv = chr.getInventory(MapleInventoryType.ETC);
        for (Item item : iv.list()) {
            if (item.getPosition() < 100) {
                addItemInfo(mplew, item, false, false, false, false, chr);
            }
        }
        mplew.write(0); // start of cash inventory
        iv = chr.getInventory(MapleInventoryType.CASH);
        for (Item item : iv.list()) {
            addItemInfo(mplew, item, false, false, false, false, chr);
        }
        mplew.write(0); // start of extended slots
        for (int i = 0; i < chr.getExtendedSlots().size(); i++) {
            mplew.writeInt(i);
            mplew.writeInt(chr.getExtendedSlot(i));
            for (Item item : chr.getInventory(MapleInventoryType.ETC).list()) {
                if (item.getPosition() > (i * 100 + 100) && item.getPosition() < (i * 100 + 200)) {
                    addItemInfo(mplew, item, false, false, false, true, chr);
                }
            }
            mplew.writeInt(-1);
        }
        mplew.writeInt(-1);
        mplew.writeInt(0); // 0x40000000 Foreach : Int + Long
        mplew.writeInt(0); // 0x400 Foreach : Long + Long
        mplew.write(0); // 0x20000000 if got, then below
        mplew.write(1);
    }

    public static final void addCharStats(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeInt(chr.getId()); // character id
        mplew.writeAsciiString(chr.getName(), 13);
        mplew.write(chr.getGender()); // gender (0 = male, 1 = female)
        mplew.write(chr.getSkinColor()); // skin color
        mplew.writeInt(chr.getFace()); // face
        mplew.writeInt(chr.getHair()); // hair
        if (GameConstants.GMS) {
            mplew.writeZeroBytes(24);
        }
        mplew.write(chr.getLevel()); // level
        mplew.writeShort(chr.getJob()); // job
        chr.getStat().connectData(mplew);
        mplew.writeShort(chr.getRemainingAp()); // remaining ap
        if (GameConstants.isEvan(chr.getJob()) || GameConstants.isResist(chr.getJob()) || GameConstants.isMercedes(chr.getJob())) {
            final int size = chr.getRemainingSpSize();
            mplew.write(size);
            for (int i = 0; i < chr.getRemainingSps().length; i++) {
                if (chr.getRemainingSp(i) > 0) {
                    mplew.write(i + 1);
                    mplew.write(chr.getRemainingSp(i));
                }
            }
        } else {
            mplew.writeShort(chr.getRemainingSp()); // remaining sp
        }
        mplew.writeInt(chr.getExp()); // exp
        mplew.writeInt(chr.getFame()); // fame
        mplew.writeInt(chr.getGachExp()); // Gachapon exp
        if (!GameConstants.GMS) {
            mplew.writeLong(0); //something. dunno
        }
        mplew.writeInt(chr.getMapId()); // current map id
        mplew.write(chr.getInitialSpawnpoint()); // spawnpoint
        if (GameConstants.GMS) {
            mplew.writeInt(0); //ive no idea, 8F 5C 00 00 or 0C F8 00 00
        }
        mplew.writeShort(chr.getSubcategory()); //1 here = db
	if (GameConstants.isDemon(chr.getJob())) {
	    mplew.writeInt(chr.getDemonMarking()); // wut
	}
        mplew.write(chr.getFatigue());
        mplew.writeInt(GameConstants.getCurrentDate());
        for (MapleTraitType t : MapleTraitType.values()) {
            mplew.writeInt(chr.getTrait(t).getTotalExp());
        } 
        mplew.writeZeroBytes(12); 
	mplew.writeInt(chr.getStat().pvpExp); //pvp exp
	mplew.write(chr.getStat().pvpRank); //pvp rank
	mplew.writeInt(chr.getBattlePoints()); //pvp points
	mplew.write(5); //idk
	mplew.writeInt(0); // JUMP
        mplew.writeInt(0x95FB64D5);
        mplew.writeInt(0x137);
    }

    public static final void addCharLook(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr, final boolean mega) {
        mplew.write(chr.getGender());
        mplew.write(chr.getSkinColor());
        mplew.writeInt(chr.getFace());
        mplew.writeInt(chr.getJob());
        mplew.write(mega ? 0 : 1);
        mplew.writeInt(chr.getHair());

        final Map<Byte, Integer> myEquip = new LinkedHashMap<Byte, Integer>();
        final Map<Byte, Integer> maskedEquip = new LinkedHashMap<Byte, Integer>();
        MapleInventory equip = chr.getInventory(MapleInventoryType.EQUIPPED);

        for (final Item item : equip.newList()) {
            if (item.getPosition() < -127) { //not visible
                continue;
            }
            byte pos = (byte) (item.getPosition() * -1);

            if (pos < 100 && myEquip.get(pos) == null) {
                myEquip.put(pos, item.getItemId());
            } else if (pos > 100 && pos != 111) {
                pos = (byte) (pos - 100);
                if (myEquip.get(pos) != null) {
                    maskedEquip.put(pos, myEquip.get(pos));
                }
                myEquip.put(pos, item.getItemId());
            } else if (myEquip.get(pos) != null) {
                maskedEquip.put(pos, item.getItemId());
            }
        }
        for (final Entry<Byte, Integer> entry : myEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF); // end of visible itens
        // masked itens
        for (final Entry<Byte, Integer> entry : maskedEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF); // ending markers

        final Item cWeapon = equip.getItem((byte) -111);
        mplew.writeInt(cWeapon != null ? cWeapon.getItemId() : 0);
	mplew.write(GameConstants.isMercedes(chr.getJob()) ? 1 : 0); // mercedes ears
        mplew.writeInt(0);
        mplew.writeLong(0);
	if (GameConstants.isDemon(chr.getJob())) {
	    mplew.writeInt(chr.getDemonMarking()); // wut
	}
    }

    public static final void addExpirationTime(final MaplePacketLittleEndianWriter mplew, final long time) {
        mplew.writeLong(getTime(time));
    }

    public static final void addItemInfo(final MaplePacketLittleEndianWriter mplew, final Item item, final boolean zeroPosition, final boolean leaveOut) {
        addItemInfo(mplew, item, zeroPosition, leaveOut, false, false, null);
    }

    public static final void addItemInfo(final MaplePacketLittleEndianWriter mplew, final Item item, final boolean zeroPosition, final boolean leaveOut, final boolean trade) {
        addItemInfo(mplew, item, zeroPosition, leaveOut, trade, false, null);
    }

    public static final void addItemInfo(final MaplePacketLittleEndianWriter mplew, final Item item, final boolean zeroPosition, final boolean leaveOut, final boolean trade, final boolean bagSlot, final MapleCharacter chr) {
        short pos = item.getPosition();
        if (zeroPosition) {
            if (!leaveOut) {
                mplew.write(0);
            }
        } else {
            if (pos <= -1) {
                pos *= -1;
                if (pos > 100 && pos < 1000) {
                    pos -= 100;
                }
            }
            if (bagSlot) {
                mplew.writeInt((pos % 100) - 1);
            } else if (!trade && item.getType() == 1) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
        }
        mplew.write(item.getPet() != null ? 3 : item.getType());
        mplew.writeInt(item.getItemId());
        boolean hasUniqueId = item.getUniqueId() > 0 && !GameConstants.isMarriageRing(item.getItemId()) && item.getItemId() / 10000 != 166;
        mplew.write(hasUniqueId ? 1 : 0);
        if (hasUniqueId) {
            mplew.writeLong(item.getUniqueId());
        }

        if (item.getPet() != null) { // Pet
            addPetItemInfo(mplew, item, item.getPet(), true);
        } else {
            addExpirationTime(mplew, item.getExpiration());
            mplew.writeInt(chr == null ? -1 : chr.getExtendedSlots().indexOf(item.getItemId()));
            if (item.getType() == 1) {
                final Equip equip = (Equip) item;
                mplew.write(equip.getUpgradeSlots());
                mplew.write(equip.getLevel());
                mplew.writeShort(equip.getStr());
                mplew.writeShort(equip.getDex());
                mplew.writeShort(equip.getInt());
                mplew.writeShort(equip.getLuk());
                mplew.writeShort(equip.getHp());
                mplew.writeShort(equip.getMp());
                mplew.writeShort(equip.getWatk());
                mplew.writeShort(equip.getMatk());
                mplew.writeShort(equip.getWdef());
                mplew.writeShort(equip.getMdef());
                mplew.writeShort(equip.getAcc());
                mplew.writeShort(equip.getAvoid());
                mplew.writeShort(equip.getHands());
                mplew.writeShort(equip.getSpeed());
                mplew.writeShort(equip.getJump());
                mplew.writeMapleAsciiString(equip.getOwner());
                mplew.writeShort(equip.getFlag());
                mplew.write(equip.getIncSkill() > 0 ? 1 : 0);
                mplew.write(Math.max(equip.getBaseLevel(), equip.getEquipLevel())); // Item level
                mplew.writeInt(equip.getExpPercentage() * 100000); // Item Exp... 10000000 = 100%
                mplew.writeInt(equip.getDurability());
                mplew.writeInt(equip.getViciousHammer());
                mplew.writeShort(equip.getPVPDamage()); //OR is it below MPR? TODOO
                if (!hasUniqueId) {
                    mplew.write(equip.getState()); //7 = unique for the lulz
                    mplew.write(equip.getEnhance());
                    mplew.writeShort(equip.getPotential1());
                    mplew.writeShort(equip.getPotential2());
                    mplew.writeShort(equip.getPotential3());
                }
		mplew.writeShort(equip.getPotential4()); // hpr is removed
		mplew.writeShort(equip.getPotential5()); // mpr is removed
                mplew.writeLong(equip.getInventoryId() <= 0 ? -1 : equip.getInventoryId()); //some tracking ID
                mplew.writeLong(getTime(-2));
                mplew.writeInt(-1); //?
            } else {
                mplew.writeShort(item.getQuantity());
                mplew.writeMapleAsciiString(item.getOwner());
                mplew.writeShort(item.getFlag());
                if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId()) || item.getItemId() / 10000 == 287) {
                    mplew.writeLong(item.getInventoryId() <= 0 ? -1 : item.getInventoryId());
		}
            }
        }
    }

    public static final void serializeMovementList(final MaplePacketLittleEndianWriter lew, final List<LifeMovementFragment> moves) {
        lew.write(moves.size());
        for (LifeMovementFragment move : moves) {
            move.serialize(lew);
        }
    }

    public static final void addAnnounceBox(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        if (chr.getPlayerShop() != null && chr.getPlayerShop().isOwner(chr) && chr.getPlayerShop().getShopType() != 1 && chr.getPlayerShop().isAvailable()) {
            addInteraction(mplew, chr.getPlayerShop());
        } else {
            mplew.write(0);
        }
    }

    public static final void addInteraction(final MaplePacketLittleEndianWriter mplew, IMaplePlayerShop shop) {
        mplew.write(shop.getGameType());
        mplew.writeInt(((AbstractPlayerStore) shop).getObjectId());
        mplew.writeMapleAsciiString(shop.getDescription());
        if (shop.getShopType() != 1) {
            mplew.write(shop.getPassword().length() > 0 ? 1 : 0); //password = false
        }
        mplew.write(shop.getItemType());
        mplew.write(shop.getSize()); //current size
        mplew.write(shop.getMaxSize()); //full slots... 4 = 4-1=3 = has slots, 1-1=0 = no slots
        if (shop.getShopType() != 1) {
            mplew.write(shop.isOpen() ? 0 : 1);
        }
    }

    public static final void addCharacterInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeInt(-1);
	mplew.writeInt(GameConstants.GMS ? -3 : -1);
        mplew.writeZeroBytes(7); //5 bytes v99
        addCharStats(mplew, chr);
        mplew.write(chr.getBuddylist().getCapacity());
        // Bless
        if (chr.getBlessOfFairyOrigin() != null) {
            mplew.write(1);
            mplew.writeMapleAsciiString(chr.getBlessOfFairyOrigin());
        } else {
            mplew.write(0);
        }
        if (chr.getBlessOfEmpressOrigin() != null) {
            mplew.write(1);
            mplew.writeMapleAsciiString(chr.getBlessOfEmpressOrigin());
        } else {
            mplew.write(0);
        }
        final MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(GameConstants.ULT_EXPLORER));
        if (ultExplorer != null && ultExplorer.getCustomData() != null) {
            mplew.write(1);
            mplew.writeMapleAsciiString(ultExplorer.getCustomData());
        } else {
            mplew.write(0);
        }
        addInventoryInfo(mplew, chr);
        addSkillInfo(mplew, chr);
        addCoolDownInfo(mplew, chr);
        addQuestInfo(mplew, chr);
        addRingInfo(mplew, chr);
        addRocksInfo(mplew, chr);
        if (GameConstants.GMS) {
            addMonsterBookInfo(mplew, chr);
	}
        chr.QuestInfoPacket(mplew); // for every questinfo: int16_t questid, string questdata
	if (!GameConstants.GMS) {
	    mplew.writeShort(0);
	}
        if (chr.getJob() >= 3300 && chr.getJob() <= 3312) { //wh
            addJaguarInfo(mplew, chr);
        }
        mplew.writeShort(0); // For each: Short + Long(filetime)
        mplew.writeShort(0); // 0x40 For each: Short + int
	for (int i = 0; i < 17; i++) {
            mplew.writeInt(0); // phantom ?
        }
	addInnerStats(mplew, chr);
        mplew.writeShort(0); // Additional item effect, Foreach: Short + Short
	addFriendshipData(mplew, chr);

    }

    public static final void addInnerStats(MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
	mplew.writeInt(0);
	mplew.write(1);
	mplew.writeShort(0);
    }
    
    public static final void addFriendshipData(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
	mplew.writeInt(0);
	mplew.writeInt(chr.getId());
	mplew.writeInt(4);
	mplew.writeInt(chr.getFriendshipPoints()); // Available Point
	for (MapleFriendshipType t : MapleFriendshipType.values()) {
	    mplew.writeInt(t.getMobId());
	    mplew.writeInt(chr.getFriendship(t).getPoints());
	}
    }

    public static final void addMonsterBookInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeInt(0); //something
        if (chr.getMonsterBook().getSetScore() > 0) {
            chr.getMonsterBook().writeFinished(mplew);
        } else {
            chr.getMonsterBook().writeUnfinished(mplew);
        }
        mplew.writeInt(chr.getMonsterBook().getSet());
        mplew.writeZeroBytes(9); //tespia lol
    }

    public static final void addPetItemInfo(final MaplePacketLittleEndianWriter mplew, final Item item, final MaplePet pet, final boolean active) {
        if (item == null) {
            mplew.writeLong(PacketHelper.getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5)));
        } else {
            PacketHelper.addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1 : item.getExpiration());
        }
        mplew.writeInt(-1);
        mplew.writeAsciiString(pet.getName(), 13);
        mplew.write(pet.getLevel());
        mplew.writeShort(pet.getCloseness());
        mplew.write(pet.getFullness());
        if (item == null) {
            mplew.writeLong(PacketHelper.getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5)));
        } else {
            PacketHelper.addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1 : item.getExpiration());
        }
        mplew.writeShort(0);
        mplew.writeShort(pet.getFlags());
        mplew.writeInt(pet.getPetItemId() == 5000054 && pet.getSecondsLeft() > 0 ? pet.getSecondsLeft() : 0); //in seconds, 3600 = 1 hr.
        mplew.writeShort(0);
        if (!GameConstants.GMS) {
            mplew.write(active ? (pet.getSummoned() ? pet.getSummonedValue() : 0) : 0); // 1C 5C 98 C6 01
            for (int i = 0; i < 4; i++) {
                mplew.write(0); //0x40 before, changed to 0?
            }
        }
    }
    
    public static final void addShopInfo(final MaplePacketLittleEndianWriter mplew, final MapleShop shop, final MapleClient c) {
	addShopInfo(mplew, shop, c, false);
    }

    public static final void addShopInfo(final MaplePacketLittleEndianWriter mplew, final MapleShop shop, final MapleClient c, final boolean rose) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (GameConstants.GMS) {
            mplew.write(shop.getRanks().size() > 0 ? 1 : 0);
            if (shop.getRanks().size() > 0) {
                mplew.write(shop.getRanks().size());
                for (Pair<Integer, String> s : shop.getRanks()) {
                    mplew.writeInt(s.left);
                    mplew.writeMapleAsciiString(s.right);
                }
            }
        }
	mplew.write(rose ? 1 : 0);
        mplew.writeShort(shop.getItems().size() + c.getPlayer().getRebuy().size()); // item count
        for (MapleShopItem item : shop.getItems()) {
            addShopItemInfo(mplew, item, shop, ii, null);
        }
        for (Item i : c.getPlayer().getRebuy()) {
            addShopItemInfo(mplew, new MapleShopItem(i.getItemId(), (int) ii.getPrice(i.getItemId()), i.getQuantity()), shop, ii, i);
        }
    }

    public static final void addShopItemInfo(final MaplePacketLittleEndianWriter mplew, final MapleShopItem item, final MapleShop shop, final MapleItemInformationProvider ii, final Item i) {
	mplew.writeLong(0);
        mplew.writeInt(item.getItemId());
        mplew.writeInt(item.getPrice());
        if (GameConstants.GMS) {
            mplew.write(0);
        }
        mplew.writeInt(item.getReqItem());
        mplew.writeInt(item.getReqItemQ());
        mplew.writeLong(0); // expiration -> min level
	//mplew.writeInt(0);
	mplew.writeInt(item.getCategory()); //category? 7 = special
	mplew.write(0); // hidden potential
	mplew.writeInt(0); //can be purchased how many times
        if (!GameConstants.isThrowingStar(item.getItemId()) && !GameConstants.isBullet(item.getItemId())) {
            mplew.writeShort(1); // stacksize o.o
	    mplew.writeShort(item.getBuyable());
        } else {
            mplew.writeZeroBytes(6);
            mplew.writeShort(BitTools.doubleToShortBits(ii.getPrice(item.getItemId())));
	    mplew.writeShort(ii.getSlotMax(item.getItemId()));
        }
//	mplew.writeShort(ii.getSlotMax(item.getItemId())); //its this for both for official servers, who cares though
	mplew.write(i == null ? 0 : 1); // todo jump
	if (i != null) {
	    addItemInfo(mplew, i, true, true);
	}
        if (GameConstants.GMS && shop.getRanks().size() > 0) {
            mplew.write(item.getRank() >= 0 ? 1 : 0);
            if (item.getRank() >= 0) {
                mplew.write(item.getRank());
            }
        }
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0); // Size
        mplew.writeInt(0);
        mplew.writeInt(MapleFriendshipType.getById(item.getItemId()).getMobId()); // Joe Joe
        mplew.writeInt(0); // Points required
        mplew.writeLong(MapleFriendshipType.Hermonniny.getMobId());
        mplew.writeLong(MapleFriendshipType.LittleDragon.getMobId());
        mplew.writeLong(MapleFriendshipType.Ika.getMobId());
    }

    public static final void addJaguarInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.write(chr.getIntNoRecord(GameConstants.JAGUAR));
        mplew.writeZeroBytes(20); //probably mobID of the 5 mobs that can be captured.
    }

    public static <E extends Buffstat> void writeSingleMask(MaplePacketLittleEndianWriter mplew, E statup) {
        for (int i = GameConstants.MAX_BUFFSTAT; i >= 1; i--) {
            mplew.writeInt(i == statup.getPosition() ? statup.getValue() : 0);
        }
    }

    public static <E extends Buffstat> void writeMask(MaplePacketLittleEndianWriter mplew, Collection<E> statups) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (E statup : statups) {
            mask[statup.getPosition() - 1] |= statup.getValue();
        }
        for (int i = mask.length; i >= 1; i--) {
            mplew.writeInt(mask[i - 1]);
        }
    }

    public static <E extends Buffstat> void writeBuffMask(MaplePacketLittleEndianWriter mplew, Collection<Pair<E, Integer>> statups) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (Pair<E, Integer> statup : statups) {
            mask[statup.left.getPosition() - 1] |= statup.left.getValue();
        }
        for (int i = mask.length; i >= 1; i--) {
            mplew.writeInt(mask[i - 1]);
        }
    }

    public static <E extends Buffstat> void writeBuffMask(MaplePacketLittleEndianWriter mplew, Map<E, Integer> statups) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (E statup : statups.keySet()) {
            mask[statup.getPosition() - 1] |= statup.getValue();
        }
        for (int i = mask.length; i >= 1; i--) {
            mplew.writeInt(mask[i - 1]);
        }
    }
}
