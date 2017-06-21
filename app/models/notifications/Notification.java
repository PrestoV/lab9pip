package models.notifications;

import actors.NotificationActorProducer;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.camel.Camel;
import akka.camel.CamelExtension;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;


@Singleton
public class Notification {
    private final ActorRef jmsProducer;

    @Inject
    public Notification(ActorSystem actorSystem) {
        Camel camel = CamelExtension.get(actorSystem);
        CamelContext camelContext = camel.context();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms", ActiveMQComponent.jmsComponentAutoAcknowledge(connectionFactory));
        camelContext.addComponent("activemq",
                ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false")
        );

        jmsProducer = actorSystem.actorOf(NotificationActorProducer.props, "jms_producer");
    }

    public void signIn(String login) {
        jmsProducer.tell(
                String.format("пользователь %s вошел в приложение", login),
                ActorRef.noSender()
        );
    }

    public void signOut(String login) {
        jmsProducer.tell(
                String.format("пользователь %s вышел из приложения", login),
                ActorRef.noSender()
        );
    }
}
