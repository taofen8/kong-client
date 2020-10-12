<p align="right">中文 | <a href="README.md">EN</a></p>

# Kong-client - A light weight java client tools for fast integrating with kong 

![](https://img.shields.io/badge/license-Apache%202.0-blue)


**Kong-client**是淘粉吧技术部开发基于java语言的Kong网关客户端工具，旨在帮助spring体系平台的用户快速接入Kong网关以及实现应用的服务化

# Features
### 接入快速、便捷
- 所有spring web工程均可接入
- 常规实现都已内置，需要用户实现或自定义的部分很少
- 配置少

### 服务自动注册
- 应用内所有已标记API服务在启动时自动注册到Kong网关服务器，无需手工维护微服务信息

### 内置调用客户端
- 提供微服务泛化调用工具，服务之间调用无需提供stub
- 客户端支持多个kong网关节点之间的负载均衡

### 监控模块
- 提供内建API，用于应用及服务监控

# Terminology
### 请求处理器
实现了KongDispatcher的实现类，我们称之为请求处理器。
一个应用内部支持多个处理器，以处理不同场景下的服务，每个处理器要负责服务的扫描收集逻辑以及请求派发逻辑，需要注意的是每个处理器定义的服务在应用全局范围内必须是唯一的，不能有服务定义冲突


### 代理策略
kong-client内置了2个处理器，分别支持了2种不同的接入场景，我们称之为代理策略
- **kongmapping**

  对需要注册网关的API接口添加`KongServiceMapping`注解，应用启动时自动扫描该注解的api注册到kong网关，调用时通过反射直接调用，简单高效，适合标准的微服务型接口以及准备接入网关的新工程
- **springmapping**
  
  无需任何注解，应用启动时自动扫描所有被Spring的RequestMapping注解标注的接口注册到kong网关，请求处理完全委托springMVC原有功能，适合已存在的spring工程快速接入网关，并支持spring自带的所有功能，如拦截器等

   如前所述，kong-client在支持2种不同代理策略的同时，也允许用户自行开发完整逻辑以支持更多的其他场景，只要实现`KongDispatcher` 或者继承`AbstractKongDispatcher`即可，





# Usage
### 配置

配置采用key\value形式的属性配置，只要能被应用环境正确加载到即可，对位置没有要求，如springboot工程只需在`application.properties`文件中定义

#### 服务端配置项
```
# kong网关服务器地址，必填
kong.config.server.admin.url

# 当前工程唯一标识，必填     
kong.config.server.app.identifier

# 允许被访问的域名，多个以逗号分割，不填则请求访问网关时不会对域名做校验，可选            
kong.config.server.route.hosts

# 代理策略，当没有自定义实现dispathcer时必须指定，可选值为：kongmapping,springmapping             
kong.config.server.proxy.strategy

# 健康检查配置，可选                           
kong.config.server.healthcheck.config

# 是否自动解析网卡ip，默认为on，upstream注册时首先会检查环境变量中是否指定了HOST_IP、HOST_PORT等参数，若未指定，则解析所有网卡，按照一定排序规则选择一个合适的ip用来注册，
若此开关为off，则只从环境变量中加载，没有设置则抛出异常
kong.config.server.address.resolve
```


#### 调用端配置项
```
# kong负载均衡节点，以逗号分割，可选
kong.config.caller.balancer.nodes

#是否开启key-auth插件处理，
kong.config.caller.keyauth.enable

#若开启key-auth插件，则指定请求头中key的名字，须与kong服务器对应
kong.config.caller.keyauth.keyname

```

### 代码改造
* 添加JAR包依赖
    ```
    <dependency>
      <groupId>com.taofen8.mid</groupId>
      <artifactId>kong-client</artifactId>
      <version>0.2.2-RELEASE</version>
    </dependency>
    ```
  
* 设置Servlet
 
    -  Springboot应用
 
       如果是springboot工程，须自定义一个空类继承自`com.taofen8.mid.kong.KongDispatcherServlet`，并标记为servlet，以下例子供参考
        ```$xslt
        @WebServlet(urlPatterns = "/*", loadOnStartup = 1)
        public class KongProxyDispatcherServlet extends KongDispatcherServlet {
        
        }
        ```
        同时，SpringBoot启动入口类添加注解`@ServletComponentScan`,例：
        ```
          @ServletComponentScan
          @MapperScan({"com.taofen8.mid.monitor.dao"})
          @SpringBootApplication
          public class KongMonitorApplication {

              public static void main(String[] args) {
                  SpringApplication.run(KongMonitorApplication.class, args);
              }
          }
        ```

    - SpringMVC应用
      
      如果是springMVC工程，须修改web.xml，将原来的`org.springframework.web.servlet.DispatcherServlet`修改为`com.taofen8.mid.kong.KongDispatcherServlet`，如：
      ```$xslt
       <servlet>
           <servlet-name>kong</servlet-name>
           <servlet-class>com.taofen8.mid.kong.KongDispatcherServlet</servlet-class>
           <load-on-startup>1</load-on-startup>
       </servlet>
       <servlet-mapping>
            <servlet-name>kong</servlet-name>
            <url-pattern>/</url-pattern>
       </servlet-mapping>
       ```
        
* 自定义处理器

   如果内置处理器无法满足需求，可按要求自行继承或实现`KongDispatcher`、`AbstractKongDispatcher`、`AbstractKongMappingDispatcher`、`AbstractSpringMappingDispatcher` 即可
  








