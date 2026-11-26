package io.github.tomthebuddhacat.record;

import net.kyori.adventure.text.Component;

public record RankRecord(String rankName, Component rankPrefix, Component tabNameColor, Component chatColor, int rankPowerLevel, int rankWeight) { }
