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

Component headerComponent = miniMessage.deserialize("<gray>Test</gray>");

Component footerComponent = miniMessage.deserialize("<gray>Test</gray>");

WrapperPlayServerPlayerListHeaderAndFooter headerAndFooter = 

    new WrapperPlayServerPlayerListHeaderAndFooter(headerComponent,
        footerComponent);
        
PacketEvents().getAPI().getPlayerManager().getUser(player.getUniqueId()).sendPacket(headerAndFooter);

}

}
```