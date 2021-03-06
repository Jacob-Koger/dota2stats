package com.jacob.dota2.dota2Application.data.history;

import com.google.gson.annotations.SerializedName;

public class MHPlayer {

    @SerializedName("account_id")
    private long accountId;
    @SerializedName("player_slot")
    private int playerSlot;
    @SerializedName("hero_id")
    private int heroId;

    public long getAccountId() {
        return accountId;
    }

    public int getPlayerSlot() {
        return playerSlot;
    }

    public int getHeroId() {
        return heroId;
    }

}
