package com.example.demo.infra.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.CassandraContainer;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class TestContainersCassandraConfig {
  public static final String ACTIVE_PROFILE = "testcontainers";

  static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      List<String> activeProfiles = Arrays.asList(configurableApplicationContext.getEnvironment().getActiveProfiles());
      log.info("coucou {}", activeProfiles.toString());
      if (testContainersProfileActivated(activeProfiles)) {
        CassandraContainer cassandra = new CassandraContainer("cassandra:3");
        cassandra.start();
        int cassandraPort = cassandra.getMappedPort(9042);
        String cassandraBaseUrl = String.format("localhost:%d", cassandraPort);
        TestPropertyValues.of(
            String.format("spring.data.cassandra.contact-points[0]=%s", cassandraBaseUrl),
            String.format("spring.data.cassandra.port=%d", cassandraPort)
        ).applyTo(configurableApplicationContext.getEnvironment());
      }
    }

    private boolean testContainersProfileActivated(List<String> activeProfiles) {
      return activeProfiles.contains(ACTIVE_PROFILE);
    }
  }
}
