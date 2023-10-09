<div align="center" style="margin-top: 5%">
  <img src="https://www.leaderos.net/apps/main/public/assets/img/brand/default.png" />
  <h1>leaderos-plugin</h1>
<p>

[![Leaderos JavaDoc](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/publish-javadoc-maven.yml/badge.svg)](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/publish-javadoc-maven.yml)
[![Java CI with Maven](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml)

</p>
</div>

🧩 The official LeaderOS plugin for Minecraft servers.

## How it's work?
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tortor quam, sodales vitae massa in, hendrerit consequat nibh. Nulla pharetra maximus ligula, sed iaculis ligula consectetur nec. Nulla non dapibus urna. Aenean odio enim, commodo aliquet ante vel, imperdiet porta eros. Duis malesuada ornare nisi sit amet auctor. Suspendisse condimentum velit et cursus dapibus. Sed eget ligula ut elit sollicitudin mollis in et nisl. Proin varius quam velit, sit amet bibendum mi fermentum eu. Maecenas elit elit, blandit vel congue a, blandit vel felis. Donec vehicula, magna ac cursus euismod, nunc erat laoreet magna, non aliquam orci mauris quis orci. Aliquam erat volutpat. Cras id metus fermentum, congue mauris eget, maximus turpis. Suspendisse efficitur urna nec nisi consectetur consectetur. Mauris erat nunc, finibus consectetur suscipit quis, facilisis eget ligula. Etiam congue erat ornare erat volutpat ultrices. Nunc gravida sapien at efficitur maximus.

## Configuration and Lang File
<details>
  <summary>config.yml</summary>

    TODO
</details>

<details>
    <summary>lang.yml</summary>

    TODO
</details>

## Commands
+ **/auth** - Lorem
+ **/webchest** - Lorem
+ **/webshop** - Lorem
+ 
## Images

### TODO

## API
[![Leaderos JavaDoc](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/publish-javadoc-maven.yml/badge.svg)](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/publish-javadoc-maven.yml)
[![Java CI with Maven](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/leaderos-net/leaderos-plugin/actions/workflows/maven.yml)

### [Javadoc](https://leaderos-net.github.io/leaderos-plugin/)

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

You can check it out the farmer javadoc [Java-Doc](https://leaderos-net.github.io/leaderos-plugin)

```java
public class Main extends JavaPlugin {
    // Returns Main class of plugin
    Main leaderosMain = LeaderOSAPI.getInstance();
    // Gets storage manager
    StorageManager storageManager = LeaderOSAPI.getStorageManager();
}
```

### Listeners

* TODO

## Used Libraries

* [spigot-api (1.20-R0.1-SNAPSHOT)](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse)
* [lombok (LATEST)](https://github.com/projectlombok/lombok)
* [BStats](https://bstats.org)
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
* [SimplixStorage](https://www.spigotmc.org/resources/simplixstorage-awesome-library-to-store-data-in-a-better-way.67286/)
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
