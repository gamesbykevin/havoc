package com.gamesbykevin.havoc.gameservices;

import com.gamesbykevin.havoc.level.Level;
import com.gamesbykevin.havoc.player.Player;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Leaderboard {

    //<string name="app_id" translatable="false">960441003299</string>

    //<!-- leaderboards -->
    public static final String LEADER_BOARD_LEVEL_1 = "CgkIo9r69fkbEAIQFA";
    public static final String LEADER_BOARD_LEVEL_2 = "CgkIo9r69fkbEAIQFQ";
    public static final String LEADER_BOARD_LEVEL_3 = "CgkIo9r69fkbEAIQFg";
    public static final String LEADER_BOARD_LEVEL_4 = "CgkIo9r69fkbEAIQFw";
    public static final String LEADER_BOARD_LEVEL_5 = "CgkIo9r69fkbEAIQGA";
    public static final String LEADER_BOARD_LEVEL_6 = "CgkIo9r69fkbEAIQGQ";
    public static final String LEADER_BOARD_LEVEL_7 = "CgkIo9r69fkbEAIQGg";
    public static final String LEADER_BOARD_LEVEL_8 = "CgkIo9r69fkbEAIQGw";
    public static final String LEADER_BOARD_LEVEL_9 = "CgkIo9r69fkbEAIQHA";
    public static final String LEADER_BOARD_LEVEL_10 = "CgkIo9r69fkbEAIQHQ";

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
                    leaderBoardId = LEADER_BOARD_LEVEL_1;
                    break;

                case 1:
                    leaderBoardId = LEADER_BOARD_LEVEL_2;
                    break;

                case 2:
                    leaderBoardId = LEADER_BOARD_LEVEL_3;
                    break;

                case 3:
                    leaderBoardId = LEADER_BOARD_LEVEL_4;
                    break;

                case 4:
                    leaderBoardId = LEADER_BOARD_LEVEL_5;
                    break;

                case 5:
                    leaderBoardId = LEADER_BOARD_LEVEL_6;
                    break;

                case 6:
                    leaderBoardId = LEADER_BOARD_LEVEL_7;
                    break;

                case 7:
                    leaderBoardId = LEADER_BOARD_LEVEL_8;
                    break;

                case 8:
                    leaderBoardId = LEADER_BOARD_LEVEL_9;
                    break;

                case 9:
                    leaderBoardId = LEADER_BOARD_LEVEL_10;
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