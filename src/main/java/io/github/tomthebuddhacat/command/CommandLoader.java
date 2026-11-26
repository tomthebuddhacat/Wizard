package io.github.tomthebuddhacat.command;

import io.github.tomthebuddhacat.service.RankService;

import java.util.ArrayList;
import java.util.List;

public class CommandLoader {

    private final List<BaseCommand> commandList;

    private final RankService rankService;

    public CommandLoader(RankService rankService) {
        this.commandList = new ArrayList<>();
        this.rankService = rankService;
    }

    public void register(BaseCommand baseCommand) {

        commandList.add(baseCommand);

        baseCommand.register(rankService);
    }
}