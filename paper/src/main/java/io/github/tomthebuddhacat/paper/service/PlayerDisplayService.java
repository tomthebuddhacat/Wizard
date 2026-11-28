package io.github.tomthebuddhacat.paper.service;

import io.github.tomthebuddhacat.common.interfaces.PlayerDisplay;
import org.bukkit.entity.Player;

public class PlayerDisplayService implements PlayerDisplay {

    private final TabListService tabListService;

    private final ScoreboardService scoreboardService;

    public PlayerDisplayService(TabListService tabListService, ScoreboardService scoreboardService) {
        this.tabListService = tabListService;
        this.scoreboardService = scoreboardService;
    }

    @Override
    public void refreshPlayerDisplay(Player player) {

        if (player == null) return;

        tabListService.updatePlayerDisplayTab(player);

        scoreboardService.updateScoreboard(player);
    }
}
