package io.github.tomthebuddhacat.service;

import io.github.tomthebuddhacat.record.RankRecord;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RankService {

    // Implemented for testing purpose only, will be removed later

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final Map<String, RankRecord> rankMap = new HashMap<>();

    public RankService() {}

    public void registerRanks() {

        rankMap.put(
                "DEFAULT", new RankRecord("DEFAULT", miniMessage.deserialize("<gray>[Default]</gray>")
        ));

        rankMap.put(
                "ADMIN", new RankRecord("ADMIN", miniMessage.deserialize("<red>[ADMIN]</red>")
        ));
    }

    public RankRecord getRank(String rankName) {
        return rankMap.getOrDefault(rankName.toUpperCase(), rankMap.get("DEFAULT"));
    }
}
