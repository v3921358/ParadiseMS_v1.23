package server;

import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import constants.MTSCSConstants;
import constants.ServerConstants;
import database.DatabaseConnection;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.guild.MapleGuild;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.Timer.BuffTimer;
import server.Timer.CheatTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.life.MobSkillFactory;
import server.life.PlayerNPC;
import server.quest.MapleQuest;

public class Start {
    public static long startTime = System.currentTimeMillis();
    public static final Start instance = new Start();

    public void run() throws InterruptedException {
        if (Boolean.parseBoolean(ServerProperties.getProperty("net.sf.odinms.world.admin")) || ServerConstants.Use_Localhost) {
            ServerConstants.Use_Fixed_IV = false;
            System.out.println("[--- Admin Mode Active ---]");
        }
        
        if (Boolean.parseBoolean(ServerProperties.getProperty("net.sf.odinms.world.logpackets"))) {
            System.out.println("[--- Logging Packets ---]");
        }  
        
        try {
            final PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
        }
        
        if (MTSCSConstants.MTS == false) {
            System.out.println("[" + ServerProperties.getProperty("net.sf.odinms.login.serverName")+ "]"); 
            System.out.println("[Maple Trade System is disable]");
            World.init();
            WorldTimer.getInstance().start();
            EtcTimer.getInstance().start();
            MapTimer.getInstance().start();
            CloneTimer.getInstance().start();
            EventTimer.getInstance().start();
            BuffTimer.getInstance().start();
            PingTimer.getInstance().start();
            System.out.println("Loading MapleGuild :::");
            MapleGuildRanking.getInstance().load();
            MapleGuild.loadAll(); //(this);
            System.out.println("Loading MapleFamily :::");
            MapleFamily.loadAll(); //(this);
            System.out.println("Loading MapleLifeFactory :::");
            MapleLifeFactory.loadQuestCounts();
            System.out.println("Loading MapleQuest :::");
            MapleQuest.initQuests();
            System.out.println("Loading MapleItemInformationProvider :::");
            MapleItemInformationProvider.getInstance().runEtc();
            System.out.println("Loading MapleMonsterInformationProvider :::");
            MapleMonsterInformationProvider.getInstance().load();
            System.out.println("Loading MapleItemInformationProvider runItems :::");
            MapleItemInformationProvider.getInstance().runItems();
            System.out.println("Loading SkillFactory :::");
            try {
                SkillFactory.load();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Loading LoginInformationProvider :::");
            LoginInformationProvider.getInstance();
            System.out.println("Loading RandomRewards :::");
            RandomRewards.load();
            System.out.println("Loading MapleOxQuizFactory :::");
            MapleOxQuizFactory.getInstance();
            System.out.println("Loading MapleCarnivalFactory :::");
            MapleCarnivalFactory.getInstance();
            System.out.println("Loading MobSkillFactory :::");
            MobSkillFactory.getInstance();
            System.out.println("Loading SpeedRunner :::");
            SpeedRunner.loadSpeedRuns();
            System.out.println("Loading MapleInventoryIdentifier :::");
            MapleInventoryIdentifier.getInstance();
            System.out.println("Loading CashItemFactory :::");
            CashItemFactory.getInstance().initialize();
//          System.out.println("Successfully loaded all data :::");
            MapleServerHandler.initiate();
            System.out.println("[Loading Login :::]");
            LoginServer.run_startup_configurations();
//          System.out.println("[Login Initialized]");

            System.out.println("[Loading Channel :::]");
            ChannelServer.startChannel_Main();
//          System.out.println("[Channel Initialized]");

            System.out.println("[Loading Cash Shop :::]");
            CashShopServer.run_startup_configurations();
    //      System.out.println("[CS Initialized]");

            CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
            Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
            World.registerRespawn();
            ShutdownServer.registerMBean();
            ServerConstants.registerMBean();
            PlayerNPC.loadAll();// touch - so we see database problems early...
            MapleMonsterInformationProvider.getInstance().addExtra();
            LoginServer.setOn(); //now or later
//          System.out.println("[Fully Initialized in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds]");
            RankingWorker.run();
        } else {
            System.out.println("[" + ServerProperties.getProperty("net.sf.odinms.login.serverName")+ "]");
            World.init();
            WorldTimer.getInstance().start();
            EtcTimer.getInstance().start();
            MapTimer.getInstance().start();
            CloneTimer.getInstance().start();
            EventTimer.getInstance().start();
            BuffTimer.getInstance().start();
            PingTimer.getInstance().start();
            System.out.println("Loading MapleGuild :::");
            MapleGuildRanking.getInstance().load();
            MapleGuild.loadAll(); //(this);
            System.out.println("Loading MapleFamily :::");
            MapleFamily.loadAll(); //(this);
            System.out.println("Loading MapleLifeFactory :::");
            MapleLifeFactory.loadQuestCounts();
            System.out.println("Loading MapleQuest :::");
            MapleQuest.initQuests();
            System.out.println("Loading MapleItemInformationProvider :::");
            MapleItemInformationProvider.getInstance().runEtc();
            System.out.println("Loading MapleMonsterInformationProvider :::");
            MapleMonsterInformationProvider.getInstance().load();
            System.out.println("Loading MapleItemInformationProvider runItems :::");
            MapleItemInformationProvider.getInstance().runItems();
            System.out.println("Loading SkillFactory :::");
            try {
                SkillFactory.load();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Loading LoginInformationProvider :::");
            LoginInformationProvider.getInstance();
            System.out.println("Loading RandomRewards :::");
            RandomRewards.load();
            System.out.println("Loading MapleOxQuizFactory :::");
            MapleOxQuizFactory.getInstance();
            System.out.println("Loading MapleCarnivalFactory :::");
            MapleCarnivalFactory.getInstance();
            System.out.println("Loading MobSkillFactory :::");
            MobSkillFactory.getInstance();
            System.out.println("Loading SpeedRunner :::");
            SpeedRunner.loadSpeedRuns();
            System.out.println("Loading MTSStorage :::");
            MTSStorage.load();
            System.out.println("Loading MapleInventoryIdentifier :::");
            MapleInventoryIdentifier.getInstance();
            System.out.println("Loading CashItemFactory :::");
            CashItemFactory.getInstance().initialize();
//          System.out.println("Successfully loaded all data :::");
            MapleServerHandler.initiate();
            System.out.println("[Loading Login :::]");
            LoginServer.run_startup_configurations();
//          System.out.println("[Login Initialized]");

            System.out.println("[Loading Channel :::]");
            ChannelServer.startChannel_Main();
//          System.out.println("[Channel Initialized]");

            System.out.println("[Loading Cash Shop :::]");
            CashShopServer.run_startup_configurations();
//          System.out.println("[CS Initialized]");

            CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
            Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
            World.registerRespawn();
            ShutdownServer.registerMBean();
            ServerConstants.registerMBean();
            PlayerNPC.loadAll();// touch - so we see database problems early...
            MapleMonsterInformationProvider.getInstance().addExtra();
            LoginServer.setOn(); //now or later
//          System.out.println("[Fully Initialized in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds]");
            RankingWorker.run();
        } 
    }

    public static class Shutdown implements Runnable {
        @Override
        public void run() {
            ShutdownServer.getInstance().run();
            ShutdownServer.getInstance().run();
        }
    }

    public static void main(final String args[]) throws InterruptedException {
        instance.run();
    }
}