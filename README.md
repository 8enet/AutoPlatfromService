# AutoPlatfromService
平台服务，简化了调用接口
spring boot + gradle + kotlin

#Run
> java8+,kotlin 1.0.1+,gradle 2.12+   
`gradle bootRun`

or:

`gradle build`
`java -jar build/libs/*.jar`

如果启用http/2.0,需要 `-Xbootclasspath/p:/path/alpn-boot-$ALPN_VERSION.jar`   
从 http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html 下载对应版本jdk的alpn-boot-$version.jar   
默认使用https/wss协议。


#Services
查询ip
https://127.0.0.1:8443/query?ip=8.8.8.8

查询手机号归属地
https://127.0.0.1:8443/query?phone=13800138000

创建用户
https://127.0.0.1:8443/user

查询用户信息
https://127.0.0.1:8443/user/1

支持通过Websocket调用接口。

write by kotlin !
