# AutoPlatfromService
平台服务，简化了调用接口
spring boot + gradle + kotlin

#Run
> java8+,kotlin 1.0.1+,gradle 2.12+   
`gradle bootRun`

如果启用http/2.0,需要 `-Xbootclasspath/p:/alpn-boot-$ALPN_VERSION.jar`

#Services
查询ip
http://127.0.0.1:8080/query?ip=8.8.8.8

查询手机号归属地
http://127.0.0.1:8080/query?phone=13800138000

创建用户
http://127.0.0.1:8080/user

查询用户信息
http://127.0.0.1:8080/user/1

支持通过Websocket调用接口。

write by kotlin !
