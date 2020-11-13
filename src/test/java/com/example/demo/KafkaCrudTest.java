package com.example.demo;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@SpringBootTest(classes = EventSourcingDemo2Application.class)
public class KafkaCrudTest {
  @Value("${app.contact-events-topic}")
  private String contactEventsTopic;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  @Autowired
  private KafkaProperties kafkaProperties;
  @Value("${spring.kafka.producer.bootstrap-servers[0]}")
  private String bootstrapServers;
  private AdminClient admin;
  @Autowired
  Environment environment;

  @BeforeEach
  void setUp() {
    Properties config = new Properties();
    config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    admin = AdminClient.create(config);
  }

  @Test
  void deleteTopic() {
    admin.deleteTopics(Collections.singletonList(contactEventsTopic));
  }

  @Test
  void name() {
    admin.createTopics(Collections.singletonList(new NewTopic(contactEventsTopic,3, (short) 1)));

    String user1Id = UUID.randomUUID().toString();
    String user2Id = UUID.randomUUID().toString();
    String user3Id = UUID.randomUUID().toString();
    for (int i = 0; i < 1000; i++) {
      kafkaTemplate.send(contactEventsTopic, user1Id, "value-" + UUID.randomUUID().toString());
      kafkaTemplate.send(contactEventsTopic, user3Id, "value-" + UUID.randomUUID().toString());
      kafkaTemplate.send(contactEventsTopic, user2Id, "value-" + UUID.randomUUID().toString());
      if (i % 10 == 0) {
        System.out.println("loop: " + i);
      }
    }
  }
}
