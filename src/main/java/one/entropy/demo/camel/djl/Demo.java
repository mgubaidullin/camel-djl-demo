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
//                .to("djl:cv/image_classification?artifactId=ai.djl.mxnet:mlp:0.0.3")
            .marshal().json(true);
    }
}
