package com.example.demo.infra.cassandra;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Configuration
@Profile("!docker-manual")
@ContextConfiguration(initializers = TestContainersCassandraConfig.Initializer.class)
public class TestContainersCassandraConfig {
  @Container
  private static CassandraContainer cassandra = new CassandraContainer("cassandra:3");
  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      int cassandraPort = cassandra.getMappedPort(9042);
      String cassandraBaseUrl = String.format("localhost:%d", cassandraPort);
      TestPropertyValues.of(
          String.format("spring.data.cassandra.contact-points[0]=%s", cassandraBaseUrl),
          String.format("spring.data.cassandra.port=%d", cassandraPort)
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }
}
