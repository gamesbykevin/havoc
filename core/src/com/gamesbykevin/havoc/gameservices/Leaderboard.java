package com.gamesbykevin.havoc.gameservices;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Leaderboard {

    //<!-- leaderboard Level 106 -->
    public static final String LEADERBOARD_LEVEL_106 = "CgkIkaSMy_4LEAIQswE";

    private static boolean isConnected(IGameServiceClient client) {
        return (client != null && client.isSessionActive());
    }

    public static void submit(IGameServiceClient client, String leaderBoardId, long time) {

        //don't continue if null
        if (client == null)
            return;

        //have to be connected
        if (!isConnected(client))
            return;

        //submit score to leader board
        if (leaderBoardId != null)
            client.submitToLeaderboard(leaderBoardId, time, null);
    }
}