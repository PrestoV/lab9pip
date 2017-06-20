package actors;

import akka.actor.Props;
import akka.camel.javaapi.UntypedProducerActor;


public class NotificationProducer extends UntypedProducerActor {
    public static Props props = Props.create(NotificationProducer.class);

    public String getEndpointUri() {
        return "jms:topic:Notifications?exchangePattern=InOnly";
    }
}
