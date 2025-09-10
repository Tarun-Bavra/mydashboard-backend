package org.thingsboard.userauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.thingsboard.userauth.dto.DeviceDataDTO;
import org.thingsboard.userauth.model.DeviceData;
import org.thingsboard.userauth.repository.DeviceDataRepository;

import java.time.LocalDateTime;

@Configuration
public class MqttConfig {

    @Value("${spring.mqtt.broker}")
    private String brokerUrl;

    @Value("${spring.mqtt.clientId}")
    private String clientId;

    @Value("${spring.mqtt.topic}")
    private String topic;

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
    private final DeviceDataRepository deviceDataRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON parser

    // Constructor-based injection (best practice)
    public MqttConfig(DeviceDataRepository deviceDataRepository) {
        this.deviceDataRepository = deviceDataRepository;
    }

    // Factory that manages MQTT connection
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        factory.setConnectionOptions(options);
        return factory;
    }

    // Channel to receive messages
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // Adapter that subscribes to MQTT broker
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    // Message handler to process incoming MQTT messages and save to PostgreSQL
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                String payload = message.getPayload().toString();
                System.out.println("Received MQTT message: " + payload);

                // Convert JSON string to DTO
                DeviceDataDTO dto = objectMapper.readValue(payload, DeviceDataDTO.class);

                // Create entity with timestamp
                DeviceData deviceData = new DeviceData(
                        dto.getTemperature(),
                        dto.getHumidity(),
                        LocalDateTime.now()
                );

                // Save to PostgreSQL
                deviceDataRepository.save(deviceData);

            } catch (Exception e) {
                logger.error("Failed to process MQTT message: {}", message.getPayload(), e);
            }

        };
    }
}
