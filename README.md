# Kong-client - A light weight java sdk  for fast integrating with kong 

![](https://img.shields.io/badge/license-Apache%202.0-blue)

kong-client is a java sdk for kong ,which is a high performance and extensible microservice
API gateway
# Prerequsites
- Spring 3.2.5+
- JDK 1.7+
- Kong 2.0.1+

# Features
- Fast Integration with springMVC:Suitable for all spring web app and work with less config
- Register services automatically
- Service Call API：Generic API for firing request
- Built-in Monitor：Exports many metrics ,like service call count 

# Terminology
### Proxy strategy
- #### kongmapping

   By this strategy, kong-client will scan all services those annotationed by `KongServiceMapping`,and register them to kong server.
   Note that, these services must be designed to  single paramter for request and response，and the parameter must be a java type instance which can be formatted to JSON. An possible service would be like this：
   ```
    @KongServiceMapping(path = "/sayHello")
    HelloResponse sayHello(HelloRequest request){
        
    }   
  ```
    
- #### springmapping
 
  This strategy is very suitable for existing springMVC projects which are wanted to be integrated to kong,and use spring's `RequestMapping` annotation on every service.
  The strategy will take advantage of all spring features,like intercepters,view resolvers,and complex request handlers etc. kong-client will only be  acted as a request proxy and do nothing invasive in request process.


Note that, 


# Usage
### Config
#### server-side
```
# kong admin url, required
# example: http://192.168.0.1:8001
kong.config.server.admin.url

# unique id for app, required 
kong.config.server.app.identifier

# hosts can be accessed for services in this app, optional       
kong.config.server.route.hosts

# proxy strategy, required        
# value: kongmapping / springmapping
kong.config.server.proxy.strategy

# healthcheck config, required                           
kong.config.server.healthcheck.config

# auto resolve ip from net interfaces ,default :on
kong.config.server.address.resolve
```

These config items would be specified if current app want to provide services to kong.

Note that，if a dispatcher were customered in spring context,
the config `kong.config.server.proxy.strategy` will be ignored.

#### client-side

```
# kong nodes for load balancer, required
# format : single node :192.168.0.1:8000 , multiple nodes: 192.168.0.1:8000 w:200,192.168.0.2:8000 w:100
kong.config.caller.balancer.nodes

# switch of key-auth plugin
# value: on /off , defualt: off
kong.config.caller.keyauth.enable

# keyname of key-auth plugin if the plugin enabled, 
# default: apikey
kong.config.caller.keyauth.keyname

```

These config items would be specifed if current app will  call kong's services.
Note that, if the app is only be a consumer of services but not a provider when `kong.config.caller.keyauth.enable` is enabled,  `kong.config.server.admin.url` and `kong.config.server.app.identifier
` also must be specified in order to register consumers automatically





### Development
* import maven dependency
    ```
    <dependency>
      <groupId>com.taofen8.mid</groupId>
      <artifactId>kong-client</artifactId>
      <version>0.2.0-RELEASE</version>
    </dependency>
    ```
  
* setup servlet
 
    -  Springboot App 
 
       Customize a class to extends `com.taofen8.mid.kong.KongDispatcherServlet`
        ```$xslt
        @WebServlet(urlPatterns = "/*", loadOnStartup = 1)
        public class KongProxyDispatcherServlet extends KongDispatcherServlet {
        
        }
        ```
        and，add annotation `@ServletComponentScan` to app entrypoint class:
        ```
          @ServletComponentScan
          @SpringBootApplication
          public class KongTestApplication {

              public static void main(String[] args) {
                  SpringApplication.run(KongTestApplication.class, args);
              }
          }
        ```

    - SpringMVC App
      
      Modify `web.xml`, change `org.springframework.web.servlet.DispatcherServlet` to `com.taofen8.mid.kong.KongDispatcherServlet`:
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
        







