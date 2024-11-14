# TSRpc Project
This is a framework that helps developers build RPC servers and clients. It achieves perfect integration with Spring Boot. Through an extensible architecture, it implements service registration, discovery, and remote invocation. Currently, the available registry is Nacos, and the supported service protocol is HTTP. By simply adding dependencies and some straightforward configurations and annotations, you can quickly build server and client applications.

For example, you can build a server application based on Spring Boot and add the following dependencies:

```xml
<dependency>
    <groupId>com.wts.tsrpc</groupId>
    <artifactId>tsrpc-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
and then you can annotate a service class with @TSService like this:

```java
@TSService(serviceId = "providerService")
public class ProviderService {

    public String primitiveService(String arg1, String arg2) {
        return arg1 + ": " + arg2;
    }
}
```

and then you can build a client application based on Spring Boot and add the following dependencies:

```xml
<dependency>
    <groupId>com.wts.tsrpc</groupId>
    <artifactId>tsrpc-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

and then you can define a client interface like this:

```java
@TSClient(applicationId = "tsRpcServerDemo", applicationVersion = "1.0", serviceId = "providerService")
public interface ProviderService {
    String primitiveService(String arg1, String arg2);
}
```

The method signatures and parameters in the client interface must be exactly the same as those in the server-side implementation class, including the fully qualified names of the parameter types.

In fact the interface annotated by @TSClient can be contained in a jar provided by the server end.

Start the nacos server, the server application, and the client application, and then you can invoke the remote service like this:

```java
@RestController
@RequestMapping("/demo")
public class DemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private ProviderService providerService;

    @RequestMapping("/test")
    public String test(String arg1, String arg2) {
//        ProviderService providerService = applicationContext.getBean("com.wts.tsrpcclientdemo.tsprcclient.ProviderService", ProviderService.class);
//        return arg1 + ": " + arg2;
        try {
            String result = providerService.primitiveService(arg1, arg2);
            result = "Client: " + result;
            return result;
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
```

The proxy object of the client interface will be injected into the controller by Spring, and you can invoke the remote service through the proxy object. You can use it directly as if it were a local service.

## Quick Start
1. Download the source code and build the project.

execute the following command in the root directory of the project:

```shell
mvn clean install
```

2. Download the sample project.

Download the sample project from the following link: [TSRpcServerDemo](https://) and [TSRpcClientDemo](https://).

3. Start the Nacos server.

navigate to the `bin` directory of the Nacos server and execute the following command:

if you are using Linux or Mac:

```shell
sh startup.sh -m standalone
```

if you are using Windows:

Open the cmd terminal, then navigate to the `bin` directory of the Nacos server and execute the following command:

```cmd
startup.cmd -m standalone
```


