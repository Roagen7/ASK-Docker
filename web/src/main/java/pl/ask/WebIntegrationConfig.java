package pl.ask;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class WebIntegrationConfig {

    @Bean
    public MessageChannel registrationRequest() {
        return new DirectChannel();
    }


    @Bean
    @Transformer(inputChannel = "registrationRequest", outputChannel = "toRabbit")
    public ObjectToJsonTransformer objectToJsonTransformer() {
        return new ObjectToJsonTransformer();
    }

    @Bean
    public SubscribableChannel toRabbit() {
        return new DirectChannel();
    }

    @Bean
    public EventDrivenConsumer rabbitConsumer(@Qualifier("toRabbit") SubscribableChannel channel,
                                              @Qualifier("rabbitOutboundEndpoint") MessageHandler handler) {
        return new EventDrivenConsumer(channel, handler);
    }

    @Bean
    public AmqpOutboundEndpoint rabbitOutboundEndpoint(AmqpTemplate amqpTemplate) {
        AmqpOutboundEndpoint adapter = new AmqpOutboundEndpoint(amqpTemplate);
        adapter.setRoutingKey("lau-kujawa");
        return adapter;
    }

}
