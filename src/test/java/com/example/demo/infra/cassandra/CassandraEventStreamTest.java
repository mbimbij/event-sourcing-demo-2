package com.example.demo.infra.cassandra;

import com.example.demo.Contact;
import com.example.demo.ContactCreatedEvent;
import com.example.demo.EventSourcingDemo2Application;
import com.example.demo.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ContextConfiguration(
    classes = EventSourcingDemo2Application.class,
    initializers = CassandraEventStreamTest.Initializer.class)
@TestPropertySource(properties = {"spring.data.cassandra.localDatacenter=datacenter1"})
class CassandraEventStreamTest {
  @Container
  private static CassandraContainer cassandra = new CassandraContainer("cassandra:3");
  @Autowired
  CassandraEventStream cassandraEventStream;
  @Autowired
  private CassandraEventRepository eventRepository;

  @Test
  void whenPublishEvent_thenEventIsPresentInCassandra() {
    // GIVEN
    UUID contactId = UUID.randomUUID();
    ZonedDateTime eventTime = ZonedDateTime.now();
    ContactCreatedEvent contactCreatedEvent = new ContactCreatedEvent(contactId,
        eventTime,
        new Contact.EmailAddress("joseph@yopmail.com"));

    // WHEN
    cassandraEventStream.publish(contactCreatedEvent);

    // THEN
    List<CassandraContactEvent> allEvents = eventRepository.findAllByContactId(contactId);
    assertThat(allEvents).isNotEmpty();
    CassandraContactEvent actualEvent = allEvents.get(0);
    CassandraContactEvent expectedEvent = new CassandraContactEvent(contactId, eventTime.toLocalDateTime(), EventType.CONTACT_CREATED, null, "joseph@yopmail.com", null, null);
    assertThat(actualEvent)
        .usingRecursiveComparison()
        .withComparatorForType(TestUtils.LOCAL_DATE_TIME_COMPARATOR, LocalDateTime.class)
        .isEqualTo(expectedEvent);
  }

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