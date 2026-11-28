# Developer Notes

## A short overview of the systems used in the plugin.

---

### 1. TabList System
- The TabList system is built using the PacketEvents API (the download link can be found in `README.md`). 
In short, the API is mainly used to create two packets: 
`WrapperPlayServerPlayerListHeaderAndFooter` and `WrapperPlayServerPlayerInfoUpdate`.


- Example:

```java

public class ExampleClass {

    public void createTabFooterAndHeader() {

        /*
         * To set up the TabList with PacketEvents,
         * we use the WrapperPlayServerPlayerListHeaderAndFooter packet.
         * It uses Components to display the header and footer.
         */

        WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter =

                new WrapperPlayServerPlayerListHeaderAndFooter(
                        /*
                         * headerComponent,
                         * footerComponent
                         */
                );

        /*
         * To send the packet to a player,
         * we use:
         * PacketEvents.getAPI()
         *     .getPlayerManager()
         *     .getUser(player.getUniqueId())
         *     .sendPacket(packet);
         */

        PacketEvents().getAPI()
                .getPlayerManager()
                .getUser(player.getUniqueId())
                .sendPacket(headerAndFooter);

    }

    public void updateScoreboard() {

        /*
         * Further own-wards, to display the player's rank (for example)
         * beside the player's name, we can use the WrapperPlayerServerInfoUpdate packet.
         * It uses userProfile and other parameters to display the output.
         */

        UserProfile userProfile = PacketEvents().getAPI()
                .getPlayerManager()
                .getUser();
        
        Component tabFormat = Component.text(player.getName());

        /*
         * You can customize the tab's format using Components
         * and manipulate it however you like.
         *
         * Keep in mind to use PacketEvents' GameMode
         * instead of Bukkit's GameMode.
         */
        
        WrapperPlayServerPlayerInfoUpdate.PlayerInfo playerInfoUpdate
                = new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                /*
                 * userProfile,
                 * isListed,
                 * playerPing,
                 * playerGameMode 
                 * tabFormatComponent,
                 * isChatSession
                 */
        );

        WrapperPlayServerPlayerInfoUpdate wrapper
                = new WrapperPlayServerPlayerInfoUpdate(
                /* 
                 * Action.UPDATE_DISPLAY_NAME,
                 * playerInfoUpdate
                 */
        );

        /*
         * Display the tab by iterating through all online players
         * and sending the packet to each player.
         */
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            PacketEvents.getAPI()
                    .getPlayerManager()
                    .getUser(onlinePlayers)
                    .sendPacket(wrapper);
        }
    }

}
```
---

---

### 2. Scoreboard Feature.

- The Scoreboard feature mainly uses three packets:
  `WrapperPlayServerScoreboardObjective`, `WrapperPlayServerDisplayScoreboard`, and `WrapperPlayServerUpdateScore`.
- `WrapperPlayServerScoreboardObjective` is used to create or update the scoreboard objective.
- `WrapperPlayServerDisplayScoreboard` is used to display the objective in a specific slot.
- `WrapperPlayServerUpdateScore` is used to add, update, or remove scoreboard lines.

- Example:

```java
public class ExampleClass {
    
    public void createScoreboard(Player player) {

        /*
         * Here, we use a Component named "scoreboardName"
         * to create a scoreboard objective with the CREATE action
         * and display its scores as integers.
         */
        WrapperPlayServerScoreboardObjective scoreboardObjective =
                new WrapperPlayServerScoreboardObjective(
                  /*
                   * scoreboardName,
                   * objectiveMode.CREATE,
                   * renderType.INTEGER
                   */
                );

        PacketEvents.getAPI()
                .getPlayerManager()
                .getUser(player)
                .sendPacket(scoreboardObjective);
    }
    
    public void displayScoreboard(Player player) {

        WrapperPlayServerDisplayScoreboard displayScoreboard =
                new WrapperPlayServerDisplayScoreboard(
                        /*
                         * position,
                         * scoreboardName
                         */
                );

        PacketEvents.getAPI()
                .getPlayerManager()
                .getUser(player)
                .sendPacket(displayScoreboard);
    }
    
    public void updateScoreboard(Player player) {

        /*
         * To add multiple lines to a scoreboard, we can use
         * the WrapperPlayServerUpdateScore packet.
         *
         * Each scoreboard line needs a unique entity name.
         * A simple way to achieve this is by using the index
         * in a for loop.
         *
         * List<String> scoreboardLines = #.getConfig().getStringList(list);
         *
         * for (int i = 0; i < scoreboardLines.size(); i++) {
         *     new WrapperPlayServerUpdateScore(
         *         "scoreboardLine_" + i,
         *     );
         * }
         *
         * The score value controls the display order of the lines.
         * You can arrange them from top to bottom or bottom to top
         * by using the index value.
         *
         * ScoreFormat is mainly used to hide the numbers
         * displayed beside the scoreboard lines.
         */
        WrapperPlayServerUpdateScore updateScore = 
                new WrapperPlayServerUpdateScore(
                        /*
                         * entityName,
                         * Action.CREATE_OR_UPDATE_ITEM
                         * objectiveName,
                         * value,
                         * entityDisplayName,
                         * ScoreFormat.blankScore()
                         */
                );

        PacketEvents.getAPI()
                .getPlayerManager()
                .getUser(player)
                .sendPacket(updateScore);
    }
}
```
---
> **Note:** This entire file was written based on my own understanding, 
> so some of the information may be incorrect.