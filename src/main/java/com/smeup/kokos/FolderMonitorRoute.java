package com.smeup.kokos;

import org.apache.camel.builder.RouteBuilder;

public class FolderMonitorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file-watch://C:\\Users\\paolo.mossini\\Desktop\\A37Config?events=CREATE,MODIFY,DELETE")
                .process(exchange -> {
                    String eventType = exchange.getIn().getHeader("CamelFileEventType").toString();
                    String fileName = exchange.getIn().getHeader("CamelFileName").toString();

                    switch (eventType) {
                        case "CREATE":
                            System.out.println("File created: " + fileName);
                            break;
                        case "MODIFY":
                            System.out.println("File modified: " + fileName);
                            break;
                        case "DELETE":
                            System.out.println("File deleted: " + fileName);
                            break;
                        default:
                            System.out.println("Unknown event: " + eventType + " for file: " + fileName);
                            break;
                    }
                });
    }
}
