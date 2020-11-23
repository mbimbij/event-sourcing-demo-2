package com.example.demo.infra.cassandra;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.CassandraContainer;

import java.util.Arrays;

@Configuration
public class TestContainersCassandraConfig {
  public static final String ACTIVE_PROFILE = "testcontainers";
  private static CassandraContainer cassandra;
  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      boolean testContainersProfileActivated = Arrays.asList(configurableApplicationContext.getEnvironment().getActiveProfiles()).contains(ACTIVE_PROFILE);
      if(testContainersProfileActivated){
        cassandra = new CassandraContainer("cassandra:3");
        cassandra.start();
        int cassandraPort = cassandra.getMappedPort(9042);
        String cassandraBaseUrl = String.format("localhost:%d", cassandraPort);
        TestPropertyValues.of(
            String.format("spring.data.cassandra.contact-points[0]=%s", cassandraBaseUrl),
            String.format("spring.data.cassandra.port=%d", cassandraPort)
        ).applyTo(configurableApplicationContext.getEnvironment());
      }
    }
  }
}
