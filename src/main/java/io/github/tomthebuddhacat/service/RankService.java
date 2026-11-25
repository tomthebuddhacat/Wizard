package io.github.tomthebuddhacat.service;

import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.repository.RankRepository;
import net.kyori.adventure.text.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RankService {

    private final Map<String, RankRecord> rankMap = new HashMap<>();

    private final Map<UUID, RankRecord> cache = new HashMap<>();

    private final RankRepository rankRepository;

    public RankService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    public void registerRanks() {

        Map<String, RankRecord> allRanks = rankRepository.getAllRanks();

        rankMap.clear();

        for (Map.Entry<String, RankRecord> entrySet : allRanks.entrySet()) {
            rankMap.put(entrySet.getKey().toUpperCase(Locale.ROOT), entrySet.getValue());
        }
    }

    public RankRecord getRankName(String rankName) {
        return rankMap.get(rankName.toUpperCase(Locale.ROOT));
    }

    public void setPlayerRank(String rankName, UUID playerUUID) {

        RankRecord rankRecord = getRankName(rankName);

        if (rankRecord != null) {
            cache.put(playerUUID, rankRecord);
        } else {
            cache.remove(playerUUID);
        }
    }

    public RankRecord getPlayerRank(UUID playerUUID) {
        return cache.get(playerUUID);
    }

    public int getRankWeight(String rankName) {
        return getRankName(rankName).rankWeight();
    }

    public Component getRankPrefix(String rankName) {
        return getRankName(rankName).rankPrefix();
    }
}
