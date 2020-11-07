package com.example.demo;

import com.example.demo.infra.InMemoryEventStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CrudTest {

  private final Contact.EmailAddress emailAddress = new Contact.EmailAddress("toto@yopmail.com");
  private EventStream eventStream;
  private ContactFactory contactFactory;

  @BeforeEach
  void setUp() {
    eventStream = new InMemoryEventStream();
    contactFactory = new ContactFactory(eventStream);
  }

  @Test
  void shouldPublishContactCreatedEvent_whenFactoryCreatesContact() {
    Contact contact = contactFactory.createContact(emailAddress);
    UUID contactUuid = contact.getId();
    ContactProjection contactProjection = new ContactProjection(contactUuid, eventStream);

    ContactCreatedEvent expectedContactCreatedEvent = new ContactCreatedEvent(contactUuid, emailAddress);
    SoftAssertions.assertSoftly(softAssertions -> {
      assertThat(eventStream.getEvents()).containsExactly(expectedContactCreatedEvent);
      assertThat(contactProjection.isCreated()).isTrue();
    });
  }

  @Test
  void givenExistingStream_thenNewProjectionHasCorrectValue() {
    UUID contactUuid = UUID.randomUUID();
    ContactCreatedEvent contactCreatedEvent = new ContactCreatedEvent(contactUuid, emailAddress);
    eventStream.publish(contactCreatedEvent);

    ContactProjection actualProjection = new ContactProjection(contactUuid, eventStream);
    ContactProjection expectedProjection = new ContactProjection(contactUuid, true, null, emailAddress, null, null);
    assertThat(actualProjection).usingRecursiveComparison().isEqualTo(expectedProjection);
  }

  @Test
  void givenCreatedContact_whenChangeEmailAddress_thenEmailAddressIsChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setEmailAddress(new Contact.EmailAddress("new@gmail.com"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        true,
        null,
        new Contact.EmailAddress("new@gmail.com"),
        null,
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo( expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangeUsername_thenUsernameChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setUsername(new Contact.Username("newUsername"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        true,
        new Contact.Username("newUsername"),
        new Contact.EmailAddress("toto@yopmail.com"),
        null,
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo( expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangeAddress_thenAddressChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setAddress(new Contact.Address("newAddress"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        true,
        null,
        new Contact.EmailAddress("toto@yopmail.com"),
        new Contact.Address("newAddress"),
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo( expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangePhoneNumber_thenPhoneNumberChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setPhoneNumber(new Contact.PhoneNumber("newPhoneNumber"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        true,
        null,
        new Contact.EmailAddress("toto@yopmail.com"),
        null,
        new Contact.PhoneNumber("newPhoneNumber"));
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo( expectedContactProjection);
  }
}
