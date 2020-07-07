
## Gradle 瘦身
### 加载外部配置文件
- 目录
```shell
config/
    application-release.properties
lib/
micro-admin-1.0.0.jar
```
- 启动命令
```shell
java -jar micro-admin-1.0.0.jar --spring.actives.profile=release
```
> 优先加载外部配置

