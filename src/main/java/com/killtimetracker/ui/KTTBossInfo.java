package com.killtimetracker.ui;

import lombok.Getter;
import net.runelite.api.ItemID;

enum KTTBossInfo {
    ZULRAH("Zulrah", ItemID.PET_SNAKELING_12940),
    CHAMBERS("Chambers",ItemID.OLMLET),
    CM_CHAMBERS("CM Chambers",ItemID.TEKTINY),
    GAUNTLET("Gauntlet",ItemID.YOUNGLLEF),
    CORRUPTED_GAUNTLET("Corrupted Gauntlet",ItemID.CORRUPTED_YOUNGLLEF),
    THEATER_OF_BLOOD("Theatre of Blood",ItemID.LIL_ZIK),
    VORKATH("Vorkath",ItemID.VORKI),
    HYDRA("Alchemical Hydra",ItemID.IKKLE_HYDRA_22748),
    GROTESQUE_GUARDIANS("Grotesque Guardians",ItemID.NOON),
    NIGHTMARE("Nightmare",ItemID.LITTLE_NIGHTMARE),
    TZTOK_JAD("TzTok-Jad",ItemID.TZREKJAD),
    TZKAL_ZUK("TzKal-Zuk",ItemID.TZREKZUK);

    @Getter
    private final String bossName;
    @Getter
    private final int icon;

    KTTBossInfo(String bossName, int icon)
    {
        this.bossName = bossName;
        this.icon = icon;
    }
}