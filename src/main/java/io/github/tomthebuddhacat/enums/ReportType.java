package io.github.tomthebuddhacat.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public enum ReportType {

    ADVERTISING(
            "Advertising",
            "report_advertising",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>promoting other servers, websites,",
                    "<gray><italic:false>or services without permission."
            )
    ),

    CHAT_ABUSE(
            "Chat Abuse",
            "report_chat_abuse",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>using inappropriate or offensive",
                    "<gray><italic:false>language in chat."
            )
    ),

    IRL_TRADING_OR_BOOSTING(
            "IRL Trading or Boosting",
            "report_boosting",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>trading in-game items or currency",
                    "<gray><italic:false>for real-world money, or boosting."
            )
    ),

    PUNISHMENT_EVASION(
            "Punishment Evasion",
            "report_evasion",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>using alternate accounts to bypass",
                    "<gray><italic:false>server punishments."
            )
    ),

    EXPLOITING(
            "Exploiting",
            "report_exploiting",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>abusing glitches or unintended",
                    "<gray><italic:false>game mechanics."
            )
    ),

    INAPPROPRIATE_USERNAME_OR_SKIN(
            "Inappropriate Username or Skin",
            "report_inappropriate_profile",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>using offensive or inappropriate",
                    "<gray><italic:false>skins or usernames."
            )
    ),

    UNFAIR_ADVANTAGE(
            "Unfair Advantage",
            "report_unfair_advantage",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>using third-party software to gain",
                    "<gray><italic:false>an unfair advantage."
            )
    ),

    TEAMING(
            "Teaming",
            "report_teaming",
            List.of(
                    "",
                    "<gray><italic:false>Report this player for",
                    "<gray><italic:false>teaming with others in",
                    "<gray><italic:false>game modes where it's not allowed."
            )
    );

    private final String reportReason;

    private final String itemId;

    private final List<String> itemLore;

    ReportType(String reportReason, String itemId, List<String> itemLore) {
        this.reportReason = reportReason;
        this.itemId = itemId;
        this.itemLore = itemLore;
    }

    public String getReportReason() {
        return reportReason;
    }

    public String getItemId() {
        return itemId;
    }

    public List<Component> getItemLore() {

        MiniMessage miniMessage = MiniMessage.miniMessage();

        List<Component> listOfComponents = new ArrayList<>();

        for (String listOfLores : itemLore) {
            listOfComponents.add(miniMessage.deserialize(listOfLores));
        }

        return listOfComponents;
    }
}
