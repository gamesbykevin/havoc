package com.gamesbykevin.havoc.gameservices;

import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Achievement {

    //<!-- achievements for beating the level -->
    public static final String ACHIEVEMENT_LEVEL_1 = "";
    public static final String ACHIEVEMENT_LEVEL_2 = "";
    public static final String ACHIEVEMENT_LEVEL_3 = "";
    public static final String ACHIEVEMENT_LEVEL_4 = "";
    public static final String ACHIEVEMENT_LEVEL_5 = "";
    public static final String ACHIEVEMENT_LEVEL_6 = "";
    public static final String ACHIEVEMENT_LEVEL_7 = "";
    public static final String ACHIEVEMENT_LEVEL_8 = "";
    public static final String ACHIEVEMENT_LEVEL_9 = "";
    public static final String ACHIEVEMENT_LEVEL_10 = "";

    //<!-- achievements for beating the level 100%-->
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_1 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_2 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_3 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_4 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_5 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_6 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_7 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_8 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_9 = "";
    public static final String ACHIEVEMENT_LEVEL_COMPLETE_10 = "";

    private static boolean isConnected(IGameServiceClient client) {
        return (client != null && client.isSessionActive());
    }

    public static void unlock(IGameServiceClient client, Level level, int index) {

        String achievementId = "";

        Player player = level.getPlayer();

        if (player.isGoal()) {

            //achievement for completing the level
            switch (index) {

                default:
                    achievementId = null;
                    break;

                case 0:
                    achievementId = ACHIEVEMENT_LEVEL_1;
                    break;

                case 1:
                    achievementId = ACHIEVEMENT_LEVEL_2;
                    break;

                case 2:
                    achievementId = ACHIEVEMENT_LEVEL_3;
                    break;

                case 3:
                    achievementId = ACHIEVEMENT_LEVEL_4;
                    break;

                case 4:
                    achievementId = ACHIEVEMENT_LEVEL_5;
                    break;

                case 5:
                    achievementId = ACHIEVEMENT_LEVEL_6;
                    break;

                case 6:
                    achievementId = ACHIEVEMENT_LEVEL_7;
                    break;

                case 7:
                    achievementId = ACHIEVEMENT_LEVEL_8;
                    break;

                case 8:
                    achievementId = ACHIEVEMENT_LEVEL_9;
                    break;

                case 9:
                    achievementId = ACHIEVEMENT_LEVEL_10;
                    break;
            }

            unlock(client, achievementId);
        }


        //achievement for completing the level 100%
        if (player.isGoal() && player.getStatEnemy() >= 100 && player.getStatItem() >= 100 && player.getStatSecret() >= 100) {

            switch (index) {

                default:
                    achievementId = null;
                    break;

                case 0:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_1;
                    break;

                case 1:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_2;
                    break;

                case 2:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_3;
                    break;

                case 3:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_4;
                    break;

                case 4:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_5;
                    break;

                case 5:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_6;
                    break;

                case 6:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_7;
                    break;

                case 7:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_8;
                    break;

                case 8:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_9;
                    break;

                case 9:
                    achievementId = ACHIEVEMENT_LEVEL_COMPLETE_10;
                    break;
            }

            //unlock the achievement
            unlock(client, achievementId);
        }
    }

    public static void unlock(IGameServiceClient client, String achievementId) {

        //don't continue if null
        if (client == null)
            return;

        //require a valid value
        if (achievementId == null || achievementId.length() < 1)
            return;

        //have to be connected
        if (!isConnected(client))
            return;

        //make sure our client is there and connected
        client.unlockAchievement(achievementId);
    }
}