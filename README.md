# Image classification service with Camel Deep Java Library and Camel Quarkus

This project demonstrates simple HTTP-service to classify images using Zoo Model.

With the aid of Apache Camel it could be implemented in a few lines of code:

```java
package one.entropy.demo.camel.djl;

import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

public class Demo extends EndpointRouteBuilder {

    public void configure() throws Exception {
        from(platformHttp("/upload"))
                .process(exchange ->
                        exchange.getIn()
                                .setBody(exchange.getIn(AttachmentMessage.class)
                                        .getAttachments().values().stream()
                                        .findFirst().get().getInputStream()
                                        .readAllBytes()))
                .to(djl("cv/image_classification").artifactId("ai.djl.mxnet:mlp:0.0.1"))
                .marshal().json(true);
    }
}
```

## How to

### Build demo
```sh
    mvn clean package
```

### Start demo
```sh
    java -jar target/camel-djl-demo-1.0.0-runner.jar
```  

### Start in dev mode
```sh
   mvn clean quarkus:dev
```  

### Call service demo
```sh
    curl -i -X POST -H "Content-Type: multipart/form-data" -F "image=@src/test/resources/10.png" http://localhost:8080/upload
```  

### Expected response
```json
    HTTP/1.1 200 OK
    Accept: */*
    Connection: keep-alive
    Content-Type: application/json
    Content-Length: 223
    User-Agent: curl/7.64.1
    Date: Sun, 12 Apr 2020 12:47:10 GMT
    
    {
      "0" : 0.99999976,
      "1" : 1.6826518E-10,
      "2" : 2.3258349E-7,
      "3" : 1.7166844E-9,
      "4" : 3.8892398E-10,
      "5" : 4.3790166E-10,
      "6" : 1.8994905E-8,
      "7" : 2.4817006E-9,
      "8" : 2.1708624E-9,
      "9" : 2.9330971E-8
    }
```  
