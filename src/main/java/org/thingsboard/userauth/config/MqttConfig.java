package org.thingsboard.userauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.thingsboard.userauth.model.Device;
import org.thingsboard.userauth.model.DeviceData;
import org.thingsboard.userauth.repository.DeviceDataRepository;
import org.thingsboard.userauth.repository.DeviceRepository;

import java.time.LocalDateTime;

@Configuration
public class MqttConfig {

    private static final Logger logger = LoggerFactory.getLogger(MqttConfig.class);

    private final DeviceDataRepository deviceDataRepository;
    private final DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @org.springframework.beans.factory.annotation.Value("${spring.mqtt.broker}")
    private String brokerUrl;

    @org.springframework.beans.factory.annotation.Value("${spring.mqtt.clientId}")
    private String clientId;

    @org.springframework.beans.factory.annotation.Value("${spring.mqtt.topic}")
    private String topic;

    public MqttConfig(DeviceDataRepository deviceDataRepository, DeviceRepository deviceRepository) {
        this.deviceDataRepository = deviceDataRepository;
        this.deviceRepository = deviceRepository;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

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

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            try {
                String payload = message.getPayload().toString();
                logger.info("Received MQTT message: {}", payload);

                // Parse JSON payload to DTO
                DeviceDataDTO dto = objectMapper.readValue(payload, DeviceDataDTO.class);

                // Fetch the device from repository using DeviceRepository
                Device device = deviceRepository.findById(dto.getDeviceId()).orElse(null);
                if (device == null) {
                    logger.warn("Device not found for id: {}", dto.getDeviceId());
                    return; // skip saving if device not found
                }

                // Save telemetry linked to the device
                DeviceData deviceData = DeviceData.builder()
                        .temperature(dto.getTemperature())
                        .humidity(dto.getHumidity())
                        .timestamp(LocalDateTime.now())
                        .device(device) // link to Device
                        .build();

                deviceDataRepository.save(deviceData);

            } catch (Exception e) {
                logger.error("Failed to process MQTT message: {}", message.getPayload(), e);
            }
        };
    }
}
