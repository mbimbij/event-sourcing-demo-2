package com.example.demo;

import com.example.demo.infra.InMemoryEventStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventSourcingDemo2ApplicationTests {

  private EventStream eventStream;
  private ContactFactory contactFactory;

  @BeforeEach
  void setUp() {
    eventStream = new InMemoryEventStream();
    contactFactory = new ContactFactory(eventStream);
  }

  @Test
  void shouldPublishContactCreatedEvent_whenFactoryCreatesContact() {
    UUID contactUuid = UUID.randomUUID();
    ContactProjection contactProjection = new ContactProjection(contactUuid, eventStream);

    Contact contact = contactFactory.createContact(contactUuid);

    SoftAssertions.assertSoftly(softAssertions -> {
      assertThat(eventStream.getEvents()).containsExactly(new ContactCreatedEvent(contactUuid));
      assertThat(contactProjection.isCreated()).isTrue();
    });
  }

}
