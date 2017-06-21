package actors;

import akka.actor.Props;
import akka.camel.javaapi.UntypedProducerActor;


public class NotificationActorProducer extends UntypedProducerActor {
    public static Props props = Props.create(NotificationActorProducer.class);

    public String getEndpointUri() {
        return "jms:topic:Notifications?exchangePattern=InOnly";
    }
}
