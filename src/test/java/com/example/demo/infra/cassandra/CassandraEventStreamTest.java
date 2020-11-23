package com.example.demo.infra.cassandra;

import com.example.demo.Contact.Address;
import com.example.demo.Contact.EmailAddress;
import com.example.demo.Contact.PhoneNumber;
import com.example.demo.Contact.Username;
import com.example.demo.*;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = {
    EventSourcingDemo2Application.class,
    TestContainersCassandraConfig.class
},initializers = TestContainersCassandraConfig.Initializer.class)
@TestPropertySource(properties = {
    "spring.data.cassandra.localDatacenter=datacenter1",
    "app.eventstore.implementation=cassandra"
})
class CassandraEventStreamTest {

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
        new EmailAddress("joseph@yopmail.com"));

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

  @Test
  void given2AggregatesEvents_whenGetEventsByAggregateId_thenGetAppropriateEvents_andSortedByEventTime() {
    // GIVEN
    UUID contactId1 = UUID.randomUUID();
    UUID contactId2 = UUID.randomUUID();
    String contact1EmailAddress = "contact1EmailAddress";
    String newContact1Username = "newContact1Username";
    String newContact1Email = "newContact1Email";
    String newContact1Address = "newContact1Address";
    String newContact1PhoneNumber = "newContact1PhoneNumber";
    String contact2EmailAddress = "contact2EmailAddress";
    String newContact2Username = "newContact2Username";
    String newContact2Email = "newContact2Email";
    String newContact2Address = "newContact2Address";
    String newContact2PhoneNumber = "newContact2PhoneNumber";
    ZonedDateTime now = ZonedDateTime.now();
    ContactCreatedEvent contact1CreatedEvent = new ContactCreatedEvent(contactId1, now.minusHours(9), new EmailAddress(contact1EmailAddress));
    ContactChangedUsernameEvent contactChangedUsernameEvent1 = new ContactChangedUsernameEvent(contactId1, now.minusHours(8), new Username(newContact1Username));
    ContactChangedEmailAddressEvent contactChangedEmailAddressEvent1 = new ContactChangedEmailAddressEvent(contactId1, now.minusHours(6), new EmailAddress(newContact1Email));
    ContactChangedAddressEvent contactChangedAddressEvent1 = new ContactChangedAddressEvent(contactId1, now.minusHours(4), new Address(newContact1Address));
    ContactChangedPhoneNumberEvent contactChangedPhoneNumberEvent1 = new ContactChangedPhoneNumberEvent(contactId1, now.minusHours(2), new PhoneNumber(newContact1PhoneNumber));
    ContactDeletedEvent contactDeletedEvent1 = new ContactDeletedEvent(contactId1, now.minusHours(1));

    ContactCreatedEvent contact2CreatedEvent = new ContactCreatedEvent(contactId2, now.minusHours(8), new EmailAddress(contact2EmailAddress));
    ContactChangedEmailAddressEvent contactChangedEmailAddressEvent2 = new ContactChangedEmailAddressEvent(contactId2, now.minusHours(7), new EmailAddress(newContact2Email));
    ContactChangedAddressEvent contactChangedAddressEvent2 = new ContactChangedAddressEvent(contactId2, now.minusHours(6), new Address(newContact2Address));
    ContactChangedPhoneNumberEvent contactChangedPhoneNumberEvent2 = new ContactChangedPhoneNumberEvent(contactId2, now.minusHours(5), new PhoneNumber(newContact2PhoneNumber));
    ContactChangedUsernameEvent contactChangedUsernameEvent2 = new ContactChangedUsernameEvent(contactId2, now.minusHours(4), new Username(newContact2Username));

    cassandraEventStream.publish(contactChangedPhoneNumberEvent1);
    cassandraEventStream.publish(contactChangedUsernameEvent1);
    cassandraEventStream.publish(contactChangedEmailAddressEvent1);
    cassandraEventStream.publish(contactDeletedEvent1);
    cassandraEventStream.publish(contact1CreatedEvent);
    cassandraEventStream.publish(contactChangedAddressEvent1);

    cassandraEventStream.publish(contact2CreatedEvent);
    cassandraEventStream.publish(contactChangedEmailAddressEvent2);
    cassandraEventStream.publish(contactChangedAddressEvent2);
    cassandraEventStream.publish(contactChangedPhoneNumberEvent2);
    cassandraEventStream.publish(contactChangedUsernameEvent2);

    // WHEN
    Iterable<INotifyDomainEvent> contactEvents1 = cassandraEventStream.getEvents(contactId1);

    // THEN
    assertThat(contactEvents1).hasSize(6);
    List<INotifyDomainEvent> contactEventsList1 = new ArrayList<>();
    contactEvents1.forEach(contactEventsList1::add);

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(contactEventsList1.get(0))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contact1CreatedEvent);
      softAssertions.assertThat(contactEventsList1.get(1))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contactChangedUsernameEvent1);
      softAssertions.assertThat(contactEventsList1.get(2))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contactChangedEmailAddressEvent1);
      softAssertions.assertThat(contactEventsList1.get(3))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contactChangedAddressEvent1);
      softAssertions.assertThat(contactEventsList1.get(4))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contactChangedPhoneNumberEvent1);
      softAssertions.assertThat(contactEventsList1.get(5))
          .usingRecursiveComparison()
          .withComparatorForType(TestUtils.ZONED_DATE_TIME_COMPARATOR, ZonedDateTime.class)
          .isEqualTo(contactDeletedEvent1);
    });
  }

}