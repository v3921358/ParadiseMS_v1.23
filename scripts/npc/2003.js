/** Author: ZeaL
	NPC Name: 		ZeaL
	Map(s): 		Entrance to Free Market (910000000)
	Description: 		Fashion NPC
*/
var status = -1;
var I;
var Iq;
var Ia;
var Ie;

var nI;
var nIq;
var nIa;
var nIe;

function start() {
		/*
		WeaponI = Item id
		WeaponIq = Quantity to sell
		WeaponIa = Price
		WeaponIe = Expiry (number of days)
		*/
		
		WeaponI = Array(1702020, 1702024, 1702063, 1702064, 1702065, 1702118, 1702119, 1702120, 1702121, 1702155, 1702217, 1702223, 1702263, 1702295, 1702344, 1702299, 1702009, 1702211, 1702174, 1702277);
		WeaponIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		WeaponIa = Array(6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500, 6500);
		WeaponIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30 ,30 ,30);
		
		HatI = Array(1002343, 1002849, 1002233, 1002234, 1002228, 1002745, 1002293, 1002294, 1002280, 1002333, 1002568, 1002888, 1002342, 1002343, 1002774, 1002667, 1002477, 1002478, 1002575, 1002576, 1002721, 1002877, 1003054, 1003070, 1002224, 1003532, 1001032, 1001031, 1002031, 1002771, 1002796);
		HatIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		HatIa = Array(6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000,6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000, 6000);
		HatIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		TopI = Array(1040101, 1042142, 1042131, 1042122, 1042188, 1042169, 1042041, 1042178, 1042198, 1042183, 1042184, 1042125, 1042096, 1042101, 1042173, 1042105, 1042177, 1042149, 1040119, 1042157, 1042036, 1042037, 1042023, 1040131, 1042164);
		TopIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		TopIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		TopIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);		
		
		BottomI = Array(1062047, 1061126, 1062053, 1061067, 1060108, 1042025, 1062094);
		BottomIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		BottomIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		BottomIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		OverallI = Array(1050141, 1050146, 1050110, 1050186, 1050187, 1051123, 1051100, 1051192, 1052154, 1052179, 1052168, 1052199, 1052306, 1050118, 1050119, 1051131);
		OverallIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		OverallIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		OverallIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		GlovesI = Array(1082249, 1082229, 1082170, 1082171, 1082161, 1082162, 1082173, 1082231);
		GlovesIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		GlovesIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		GlovesIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		CapeI = Array(1102108, 1102067, 1102095, 1102096, 1102063, 1102112, 1102148, 1102149, 1102151, 1102184, 1102336, 1102072, 1102326, 1102073, 1102093, 1102186);
		CapeIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		CapeIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		CapeIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		ShoeI = Array(1072186, 1072189, 1072235, 1072257, 1072258, 1072259, 1072260, 1072517, 1071014, 1071013, 1072233, 1072013, 1072057, 1072058);
		ShoeIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		ShoeIa = Array(4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500, 4500);
		ShoeIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		RingI = Array(1112908, 1112908, 1112908, 1112924, 1112100, 1112925, 1112926, 1112101, 1112226, 1112208, 1112218, 1112107, 1112137, 1112240);
		RingIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		RingIa = Array(600, 9000, 15000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000, 4000);
		RingIe = Array(1, 30, 90, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		FaceI = Array(1012027, 1012105, 1012096, 1012208, 1012074, 1012080, 1012055, 1012040, 1012083, 1012024, 1012137);
		FaceIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		FaceIa = Array(3500, 3500, 3500, 3500, 3500, 3500, 3500, 3500, 3500, 3500, 3500);
		FaceIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);

		EyeI = Array(1022044, 1022033, 1022014, 1022043, 1022061, 1022062, 1022045, 1022050, 1022102);
		EyeIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1);
		EyeIa = Array(3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000);
		EyeIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		EarI = Array(1032052, 1032053, 1032063);
		EarIq = Array(1, 1, 1);
		EarIa = Array(2500, 2500, 2500);
		EarIe = Array(30, 30, 30);
		
		//Throwing Stars
		TsI = Array(5021000, 5021001, 5021002, 5021003, 5021004, 5021005, 5021006, 5021007, 5021008, 5021009, 5021010, 5021011, 5021012, 5021013, 5021014, 5021015, 5021016, 5021017, 5021019, 5021020, 5021021, 5021022, 5021023, 5021025, 5021026);
		TsIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		TsIa = Array(2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500);
		TsIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);

		TransparentI = Array(1002186, 1032024, 1022048, 1012057, 1072153, 1082102, 1092056, 1102039, 1702190, 1702099, 1702224);
		TransparentIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		TransparentIa = Array(6440, 7000, 6000, 3200, 6000, 6000, 7000, 4000, 6000, 6000, 8000);
		TransparentIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
		ReleaseI = Array(5021000, 5021001, 5021002, 5021003, 5021004, 5021005, 5021006, 5021007, 5021008, 5021009, 5021010, 5021011, 5021012, 5021013, 5021014, 5021015, 5021016, 5021017, 5021019, 5021020, 5021021, 5021022, 5021023, 5021025, 5021026);
		ReleaseIq = Array(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		ReleaseIa = Array(2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500, 2500);
		ReleaseIe = Array(30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30);
		
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		cm.sendSimple("Hi there, I'm the #rCash Fashion#k Npc. I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. You could sure use some makeover...\r\n\r\n#b#L999#Latest Release#k #r#l#k\r\n#b#L0#Set Equipments#k #r#l#k\r\n#b#L1#Weapons#k #r#l#k\r\n#b#L2#Armour#k #r#l#k\r\n#b#L3#Accesory#k #r#l#k\r\n#b#L4#ETC#k #r#l#k");
	} else if (status == 1) {
		if (selection == 999){
			cm.sendSimple("#rLatest Release#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L1000#Latest Release#k");
		} else if (selection == 0) {
			cm.sendSimple("#Set Equipments#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L1#Transparent Equipments#k");
		} else if (selection == 1) {
			cm.sendSimple("#rWeapons#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L101#Weapons#k\r\n#b#L102#Throwing Stars#k#l#k");
		} else if (selection == 2){
			cm.sendSimple("#rArmour#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L201#Helmet#k #r#l#k\r\n#b#L202#Top#k #r#l#k\r\n#b#L203#Bottom#k #r#l#k\r\n#b#L204#Overall#k #r#l#k\r\n#b#L205#Gloves#k #r#l#k\r\n#b#L206#Cape#k #r#l#k\r\n#b#L207#Shoe#k");
		} else if (selection == 3) {
			cm.sendSimple("#rAccessory#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L301#Ring#k#l#k\r\n#b#L302#Face#k #r#l#k\r\n#b#L303#Eye#k #r#l#k\r\n#b#L304#Earring#k");
		} else if (selection == 4) {
			cm.sendSimple("#ETC#k : I see that you have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rCash#k. Here are some options below...\r\n\r\n#b#L401#.#k#l#k");
		}
	} else if (status == 2) {
		if (selection == 101){
			nI = WeaponI;
			nIq = WeaponIq;
			nIa = WeaponIa;
			nIe = WeaponIe;
		} else if (selection == 102){
			nI = TsI;
			nIq = TsIq;
			nIa = TsIa;
			nIe = TsIe;
		} else if (selection == 201){
			nI = HatI;
			nIq = HatIq;
			nIa = HatIa;
			nIe = HatIe;
		} else if (selection == 202){
			nI = TopI;
			nIq = TopIq;
			nIa = TopIa;
			nIe = TopIe;
		} else if (selection == 203){
			nI = BottomI;
			nIq = BottomIq;
			nIa = BottomIa;
			nIe = BottomIe;
		} else if (selection == 204){
			nI = OverallI;
			nIq = OverallIq;
			nIa = OverallIa;
			nIe = OverallIe;
		} else if (selection == 205){
			nI = GlovesI;
			nIq = GlovesIq;
			nIa = GlovesIa;
			nIe = GlovesIe;
		} else if (selection == 206){
			nI = CapeI;
			nIq = CapeIq;
			nIa = CapeIa;
			nIe = CapeIe;
		} else if (selection == 207){
			nI = ShoeI;
			nIq = ShoeIq;
			nIa = ShoeIa;
			nIe = ShoeIe;
		} else if (selection == 301){
			nI = RingI;
			nIq = RingIq;
			nIa = RingIa;
			nIe = RingIe;
		} else if (selection == 302){
			nI = FaceI;
			nIq = FaceIq;
			nIa = FaceIa;
			nIe = FaceIe;
		} else if (selection == 303){
			nI = EyeI;
			nIq = EyeIq;
			nIa = EyeIa;
			nIe = EyeIe;
		} else if (selection == 304){
			nI = EarI;
			nIq = EarIq;
			nIa = EarIa;
			nIe = EarIe;
		} else if (selection == 1) {
			nI = TransparentI;
			nIq = TransparentIq;
			nIa = TransparentIa;
			nIe = TransparentIe;
		} else {
			nI = ReleaseI;
			nIq = ReleaseIq;
			nIa = ReleaseIa;
			nIe = ReleaseIe;
		}
			var selStr = "Maybe you could trade me some #rCash#k? I have lots of great I for you...#k#b\r\n\r\n";
			for (var i = 0; i < nI.length; i++) {
				selStr += "#L" + i + "##i" + nI[i] + ":##t" + nI[i] + "# #r(" + nIe[i] + " days, " +  (nIa[i]) + " Cash)#b#l\r\n";
			}
			cm.sendSimple(selStr + "#k\n");
	} else if (status == 3) {
		if (cm.getPlayer().getCSPoints(1) < nIa[selection]) {
			cm.sendOk("You don't have enough #rCash#k.. I NEED #rCash#k!");
		} else if (!cm.canHold(nI[selection], nIq[selection])) {
			cm.sendOk("You don't have the inventory space to hold it. I must be legit and make this a fair trade... so hurry up and free your inventory so I can get my #rCash#k!");
		} else {
			cm.getPlayer().modifyCSPoints(1, -(cm.isGMS() ? (nIa[selection] / 2) : (nIa[selection])), true);
			if (nIe[selection] > 0) {
				cm.gainItemPeriod(nI[selection], nIq[selection], nIe[selection]);
			} else {
				cm.gainItem(nI[selection], nIq[selection]);
			}
			cm.sendOk("Thanks a lot for the #rCash#k! Hehe...");
		}
		cm.dispose();
	}
}