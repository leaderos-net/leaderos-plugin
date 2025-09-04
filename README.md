<div align="center" style="margin-top: 5%">
  <img src="https://www.leaderos.net/apps/main/public/assets/img/brand/default.png" />
  <h1>leaderos-plugin</h1>
<p>

[![Java CI with Maven](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml)

</p>
</div>

🧩 The official LeaderOS plugin for Minecraft servers. We currently support Bukkit, Spigot, Paper, Folia, Bungeecord, Velocity. Offer features such as Web Store, Bazaar, Credits, Authentication and ensure product delivery after purchase.

## Download
[https://www.spigotmc.org/resources/leaderos-net-official-plugin.105496/](https://www.spigotmc.org/resources/leaderos-net-official-plugin.105496/)

## Wiki
🇺🇸 English: [https://docs.leaderos.net/integrations/leaderos-plugin](https://docs.leaderos.net/integrations/leaderos-plugin)

🇹🇷 Türkçe: [https://destek.leaderos.com.tr/uecretsiz-hizmetler/leaderos-plugin](https://destek.leaderos.com.tr/uecretsiz-hizmetler/leaderos-plugin)

## API

### Maven:

Add this to your pom.xml if you use in maven.

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.leaderos-net</groupId>
    <artifactId>leaderos-plugin</artifactId>
    <version>{RELEASE-VERSION}</version>
</dependency>
```

### Gradle:

Add this to your build.gradle if you use in gradle.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
```groovy
dependencies {
    implementation 'com.github.leaderos-net:leaderos-plugin:{RELEASE-VERSION}'
}
```

### How to use?

leaderos-plugin has good javadoc.

You can check it out the plugin javadoc [Java-Doc](https://leaderos-net.github.io/leaderos-plugin)

```java
public class Main extends JavaPlugin {
    // Gets module manager
    ModuleManager moduleManager = LeaderOSAPI.getModuleManager();
    
    // Gets credit manager
    CreditManager creditManager = LeaderOSAPI.getCreditManager();
}
```

## Used Libraries

* [spigot-api (1.20-R0.1-SNAPSHOT)](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse)
* [lombok (LATEST)](https://github.com/projectlombok/lombok)
* [BStats](https://bstats.org)
* [triumph-cmds](https://github.com/TriumphTeam/triumph-cmds)
* [okaeri-configs](https://github.com/OkaeriPoland/okaeri-configs)
* [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
* [InventoryGUI](https://github.com/Phoenix616/InventoryGui)
* [XSeries](https://github.com/CryptoMorin/XSeries)
* [NBTApi](https://www.spigotmc.org/resources/nbt-api.7939/)
* [AuthLib](https://mvnrepository.com/artifact/com.mojang/authlib/1.5.25)
* [NBT-API](https://github.com/tr7zw/Item-NBT-API)

## Contributing

We welcome contributions from the community! If you would like to contribute, please follow these guidelines:

1. Fork the repository and clone it to your local machine.
2. Create a new branch for your feature or bug fix.
3. Make your changes, and ensure that your code is well-tested.
4. Create a pull request with a detailed description of your changes.

By contributing to this project, you agree to abide by the [Code of Conduct](CODE_OF_CONDUCT.md).

## License

This project is licensed under the [MIT License](LICENSE).
