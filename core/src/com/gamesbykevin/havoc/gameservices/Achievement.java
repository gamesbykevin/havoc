package com.gamesbykevin.havoc.gameservices;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class Achievement {

    //<!-- achievement Level 1 -->
    public static final String ACHIEVEMENT_LEVEL_1 = "CgkIkaSMy_4LEAIQAQ";

    private static boolean isConnected(IGameServiceClient client) {
        return (client != null && client.isSessionActive());
    }

    public static void unlock(IGameServiceClient client, String achievementId) {

        //don't continue if null
        if (client == null)
            return;

        //make sure our client is there and connected
        if (isConnected(client)) {
            client.unlockAchievement(achievementId);
        }
    }
}