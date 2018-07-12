package tools.packet;

import client.MapleCharacter;
import client.MapleStat;
import client.inventory.Item;
import client.inventory.MaplePet;
import constants.GameConstants;
import handling.SendPacketOpcode;
import java.util.List;
import server.movement.LifeMovementFragment;
import tools.data.MaplePacketLittleEndianWriter;

public class PetPacket {

    public static final byte[] updatePet(final MaplePet pet, final Item item, final boolean active) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MODIFY_INVENTORY_ITEM.getValue());
        mplew.write(0);
        mplew.write(2);
	mplew.write(0);
        mplew.write(3);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(0);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(3);
        mplew.writeInt(pet.getPetItemId());
        mplew.write(1);
        mplew.writeLong(pet.getUniqueId());
        PacketHelper.addPetItemInfo(mplew, item, pet, active);
        return mplew.getPacket();
    }

    public static final byte[] showPet(final MapleCharacter chr, final MaplePet pet, final boolean remove, final boolean hunger) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(chr.getId());
	if (GameConstants.GMS) {
	    mplew.write(chr.getPetIndex(pet));
	} else {
	    mplew.writeInt(chr.getPetIndex(pet));
	}
        if (remove) {
            mplew.write(0);
            mplew.write(hunger ? 1 : 0);
        } else {
            mplew.write(1);
            mplew.write(0); //1?
            mplew.writeInt(pet.getPetItemId());
            mplew.writeMapleAsciiString(pet.getName());
            mplew.writeLong(pet.getUniqueId());
            mplew.writeShort(pet.getPos().x);
            mplew.writeShort(pet.getPos().y - 20);
            mplew.write(pet.getStance());
            mplew.writeInt(pet.getFh());
	    mplew.writeInt(132); // WTF?AGAIN,WHAT IS THIS
        }
        return mplew.getPacket();
    }

    public static final byte[] removePet(final int cid, final int index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(cid);
	if (GameConstants.GMS) {
	    mplew.write(index);
	} else {
	    mplew.writeInt(index);
	}
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static final byte[] movePet(final int cid, final int pid, final byte slot, final List<LifeMovementFragment> moves) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
        mplew.writeInt(cid);
	if (GameConstants.GMS) {
	    mplew.write(slot);
	} else {
	    mplew.writeInt(slot);
	}
	mplew.writeLong(pid);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static final byte[] petChat(final int cid, final int un, final String text, final byte slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
        mplew.writeInt(cid);
	if (GameConstants.GMS) {
	    mplew.write(slot);
	} else {
	    mplew.writeInt(slot);
	}
        mplew.writeShort(un);
        mplew.writeMapleAsciiString(text);
        mplew.write(0); //hasQuoteRing

        return mplew.getPacket();
    }

    public static final byte[] commandResponse(final int cid, final byte command, final byte slot, final boolean success, final boolean food) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
        mplew.writeInt(cid);
	if (GameConstants.GMS) {
	    mplew.write(slot);
	} else {
	    mplew.writeInt(slot);
	}
	mplew.write(command == 1 ? 1 : 0);
	mplew.write(command);
        if (command == 1) {
	    mplew.write(0);

        } else {
            mplew.writeShort(success ? 1 : 0);
	}
        return mplew.getPacket();
    }

    public static final byte[] showOwnPetLevelUp(final byte index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(6);
        mplew.write(0);
        mplew.writeInt(index); // Pet Index

        return mplew.getPacket();
    }

    public static final byte[] showPetLevelUp(final MapleCharacter chr, final byte index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(6);
        mplew.write(0);
        mplew.writeInt(index);

        return mplew.getPacket();
    }

    public static final byte[] showPetUpdate(final MapleCharacter chr, final int uniqueId, final byte index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_UPDATE.getValue());
        mplew.writeInt(chr.getId());
	if (GameConstants.GMS) {
	    mplew.write(index);
	} else {
	    mplew.writeInt(index);
	}
	mplew.writeLong(uniqueId);
        mplew.write(0); //not sure, probably not it
	mplew.writeLong(0); // is this even correct?
        return mplew.getPacket();
    }

    public static final byte[] petStatUpdate(final MapleCharacter chr) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(0);
        if (GameConstants.GMS) {
            mplew.writeLong(MapleStat.PET.getValue());
        } else {
            mplew.writeInt((int)MapleStat.PET.getValue());
        }

        byte count = 0;
        for (final MaplePet pet : chr.getPets()) {
            if (pet.getSummoned()) {
                mplew.writeLong(pet.getUniqueId());
                count++;
            }
        }
        while (count < 3) {
            mplew.writeZeroBytes(8);
            count++;
        }
        mplew.write(0);
	mplew.writeShort(0);

        return mplew.getPacket();
    }
}
