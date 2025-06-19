package com.docstream.storeservice.config;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    /**
     * DeadLetter para enviar mensajes fallidos
     * @param kafkaTemplate
     * @return
     */
    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<Object, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        // Backoff para reintentos antes de enviar al DLT (3 intentos, 1s de espera)
        FixedBackOff backOff = new FixedBackOff(1000L, 3);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            System.err.printf("Error en intento #%d para mensaje: %s%n", deliveryAttempt, record.value());
        });

        // Para añadir excepciones ignoradas o tratadas especialmente:
        // errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        return errorHandler;
    }

    /**
     * Asocia el handler a KafkaListenerContainerFactory
     * @param consumerFactory
     * @param errorHandler
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DocumentProcessingEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, DocumentProcessingEvent> consumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, DocumentProcessingEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }



}
