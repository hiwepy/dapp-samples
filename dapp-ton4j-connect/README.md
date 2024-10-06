# dapp-ton4j-connect

> 基于 [Spring Boot 3.x](https://docs.spring.io/spring-boot/index.html) 、[Java SDK for The Open Network (TON)](https://github.com/neodix42/ton4j) 的 `Ton Connect` 功能示例。

### Java SDK for The Open Network (TON)

Github 地址：https://github.com/neodix42/ton4j

用于与 TON 区块链交互的 Java 库。不要忘记将 tonlibjson 库放入您的项目中.

**Maven**

```xml
<dependency>
    <groupId>io.github.neodix42</groupId>
    <artifactId>smartcontract</artifactId>
    <version>0.7.0</version>
</dependency>
```

**Jitpack**

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>io.github.neodix42</groupId>
    <artifactId>ton4j</artifactId>
    <version>0.7.0</version>
</dependency>
```

您可以单独使用每个子模块。单击下面的模块以获取更多详细信息。

* [Tonlib](https://github.com/neodix42/ton4j/blob/main/tonlib/README.md) - 使用外部 Tonlib 共享库与 TON 区块链通信。
* [SmartContract](https://github.com/neodix42/ton4j/blob/main/smartcontract/README.md) - 创建和部署自定义和预定义的智能合约。
* [Cell](https://github.com/neodix42/ton4j/blob/main/cell/README.md) - —创建、读取和操作Cell。
* [BitString](https://github.com/neodix42/ton4j/blob/main/bitstring/README.md) - 构造 bit-strings.
* [Address](https://github.com/neodix42/ton4j/blob/main/address/README.md) - 创建并解析 TON 钱包地址.
* [Mnemonic](https://github.com/neodix42/ton4j/blob/main/mnemonic/README.md) - 为 TON 区块链生成确定性密钥的有用方法.
* [Emulator](https://github.com/neodix42/ton4j/blob/main/emulator/README.md) - 用于与外部预编译模拟器共享库一起使用的包装器.
* [Liteclient](https://github.com/neodix42/ton4j/blob/main/liteclient/README.md) -  用于与外部预编译的 Lite-client 二进制文件一起使用的包装器.
* [Fift](https://github.com/neodix42/ton4j/blob/main/fift/README.md) - 用于与外部预编译的 fift 二进制文件一起使用的包装器.
* [Func](https://github.com/neodix42/ton4j/blob/main/func/README.md) - 用于与外部预编译函数二进制文件一起使用的包装器.
* [TonConnect](https://github.com/neodix42/ton4j/blob/main/tonconnect/README.md) - Ton Connect 标准的实现.
* [Utils](https://github.com/neodix42/ton4j/blob/main/utils/README.md) - 创建私钥和公钥，转换数据等。
