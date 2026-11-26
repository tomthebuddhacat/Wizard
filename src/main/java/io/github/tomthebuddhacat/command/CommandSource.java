package io.github.tomthebuddhacat.command;

import io.github.tomthebuddhacat.record.RankRecord;
import io.github.tomthebuddhacat.service.RankService;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class CommandSource {

    private final CommandSender commandSender;

    private final Player player;

    private final RankService rankService;

    public CommandSource(CommandSender commandSender, RankService rankService) {
        this.commandSender = commandSender;
        this.rankService = rankService;

        this.player = commandSender instanceof Player ? (Player) commandSender : null;
    }

    public void sendMessage(String message) {
        commandSender.sendMessage(message);
    }

    public int getCommandPermissionLevel() {

        if (player == null) {
            return 1500;
        }

        RankRecord rankRecord = rankService.getPlayerRank(player.getUniqueId());

        if (rankRecord == null) return 0;

        return rankRecord.rankPowerLevel();
    }
}