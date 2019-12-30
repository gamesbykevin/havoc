package com.gamesbykevin.havoc.gameservices;

import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Leaderboard {

    //<!-- leaderboards -->
    public static final String LEADERBOARD_LEVEL_1 = "";
    public static final String LEADERBOARD_LEVEL_2 = "";
    public static final String LEADERBOARD_LEVEL_3 = "";
    public static final String LEADERBOARD_LEVEL_4 = "";
    public static final String LEADERBOARD_LEVEL_5 = "";
    public static final String LEADERBOARD_LEVEL_6 = "";
    public static final String LEADERBOARD_LEVEL_7 = "";
    public static final String LEADERBOARD_LEVEL_8 = "";
    public static final String LEADERBOARD_LEVEL_9 = "";
    public static final String LEADERBOARD_LEVEL_10 = "";

    private static boolean isConnected(IGameServiceClient client) {
        return (client != null && client.isSessionActive());
    }

    public static void submit(IGameServiceClient client, Level level, int index) {

        String leaderBoardId;

        Player player = level.getPlayer();

        if (player.isGoal()) {

            switch (index) {

                default:
                    leaderBoardId = null;
                    break;

                case 0:
                    leaderBoardId = LEADERBOARD_LEVEL_1;
                    break;

                case 1:
                    leaderBoardId = LEADERBOARD_LEVEL_2;
                    break;

                case 2:
                    leaderBoardId = LEADERBOARD_LEVEL_3;
                    break;

                case 3:
                    leaderBoardId = LEADERBOARD_LEVEL_4;
                    break;

                case 4:
                    leaderBoardId = LEADERBOARD_LEVEL_5;
                    break;

                case 5:
                    leaderBoardId = LEADERBOARD_LEVEL_6;
                    break;

                case 6:
                    leaderBoardId = LEADERBOARD_LEVEL_7;
                    break;

                case 7:
                    leaderBoardId = LEADERBOARD_LEVEL_8;
                    break;

                case 8:
                    leaderBoardId = LEADERBOARD_LEVEL_9;
                    break;

                case 9:
                    leaderBoardId = LEADERBOARD_LEVEL_10;
                    break;
            }

            submit(client, leaderBoardId, (long)player.getTimerGame().getLapsed());
        }
    }

    public static void submit(IGameServiceClient client, String leaderBoardId, long time) {

        //don't continue if null
        if (client == null)
            return;

        //have to be connected
        if (!isConnected(client))
            return;

        //require a valid value
        if (leaderBoardId == null)
            return;

        //submit score to leader board
        client.submitToLeaderboard(leaderBoardId, time, null);
    }
}