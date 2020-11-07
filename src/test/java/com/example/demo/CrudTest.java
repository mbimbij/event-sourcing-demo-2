package com.example.demo;

import com.example.demo.infra.InMemoryContactRepository;
import com.example.demo.infra.InMemoryEventStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.Contact.State.CREATED;
import static com.example.demo.Contact.State.DELETED;
import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class CrudTest {

  private final Contact.EmailAddress emailAddress = new Contact.EmailAddress("toto@yopmail.com");
  private EventStream eventStream;
  private ContactFactory contactFactory;
  private ContactRepository contactRepository;

  @BeforeEach
  void setUp() {
    eventStream = new InMemoryEventStream();
    contactFactory = new ContactFactory(eventStream);
    contactRepository = new InMemoryContactRepository(eventStream);
  }

  @Test
  void shouldPublishContactCreatedEvent_whenFactoryCreatesContact() {
    Contact contact = contactFactory.createContact(emailAddress);
    UUID contactUuid = contact.getId();
    ContactProjection contactProjection = new ContactProjection(contactUuid, eventStream);

    ContactCreatedEvent expectedContactCreatedEvent = new ContactCreatedEvent(contactUuid, emailAddress);
    SoftAssertions.assertSoftly(softAssertions -> {
      assertThat(eventStream.getEvents()).containsExactly(expectedContactCreatedEvent);
      assertThat(contactProjection.getState()).isEqualTo(CREATED);
    });
  }

  @Test
  void givenExistingStream_thenNewProjectionHasCorrectValue() {
    UUID contactUuid = UUID.randomUUID();
    ContactCreatedEvent contactCreatedEvent = new ContactCreatedEvent(contactUuid, emailAddress);
    eventStream.publish(contactCreatedEvent);

    ContactProjection actualProjection = new ContactProjection(contactUuid, eventStream);
    ContactProjection expectedProjection = new ContactProjection(contactUuid,
        CREATED,
        null,
        emailAddress,
        null,
        null);
    assertThat(actualProjection).usingRecursiveComparison().isEqualTo(expectedProjection);
  }

  @Test
  void givenCreatedContact_whenChangeEmailAddress_thenEmailAddressIsChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setEmailAddress(new Contact.EmailAddress("new@gmail.com"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        CREATED,
        null,
        new Contact.EmailAddress("new@gmail.com"),
        null,
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo(expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangeUsername_thenUsernameChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setUsername(new Contact.Username("newUsername"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        CREATED,
        new Contact.Username("newUsername"),
        new Contact.EmailAddress("toto@yopmail.com"),
        null,
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo(expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangeAddress_thenAddressChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setAddress(new Contact.Address("newAddress"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        CREATED,
        null,
        new Contact.EmailAddress("toto@yopmail.com"),
        new Contact.Address("newAddress"),
        null);
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo(expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenChangePhoneNumber_thenPhoneNumberChanged() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setPhoneNumber(new Contact.PhoneNumber("newPhoneNumber"));
    ContactProjection contactProjection = new ContactProjection(contact.getId(), eventStream);
    ContactProjection expectedContactProjection = new ContactProjection(contact.getId(),
        CREATED,
        null,
        new Contact.EmailAddress("toto@yopmail.com"),
        null,
        new Contact.PhoneNumber("newPhoneNumber"));
    assertThat(contactProjection).usingRecursiveComparison().isEqualTo(expectedContactProjection);
  }

  @Test
  void givenCreatedContact_whenDelete_thenContactDeleted() {
    UUID contactId = contactFactory.createContact(emailAddress).getId();
    ContactProjection contactProjection = new ContactProjection(contactId, eventStream);

    contactRepository.delete(contactId);

    assertThat(contactProjection.getState()).isEqualTo(DELETED);
  }

  @Test
  void givenCreatedContact_thenCanGetAggregateById_andStateIsCorrect() {
    Contact contact = contactFactory.createContact(emailAddress);
    contact.setEmailAddress(new Contact.EmailAddress("newEmailAddress"));
    contact.setPhoneNumber(new Contact.PhoneNumber("newPhoneNumber"));

    Optional<Contact> contactOptional = contactRepository.get(contact.getId());
    assertThat(contactOptional).isPresent();
    SoftAssertions.assertSoftly(softAssertions -> {
      Contact contactFromRepository = contactOptional.get();
      softAssertions.assertThat(contactFromRepository)
          .isEqualTo(contactFromRepository);
    });
  }

  @Test
  void givenContactDeleted_whenGetById_thenReturnsOptionalEmpty() {
    UUID contactId = contactFactory.createContact(emailAddress).getId();
    contactRepository.delete(contactId);

    Optional<Contact> contactOptional = contactRepository.get(contactId);

    assertThat(contactOptional).isEmpty();
  }

  @Test
  void getProjectionAtSpecificTime() {
    UUID contactId = UUID.randomUUID();
    eventStream.publish(new ContactCreatedEvent(contactId, now(), emailAddress));
    eventStream.publish(new ContactChangedPhoneNumberEvent(contactId, now().plusHours(1), new Contact.PhoneNumber("newPhoneNumber")));

    ContactProjection expectedLatestProjection = new ContactProjection(contactId, CREATED, null, emailAddress, null, new Contact.PhoneNumber("newPhoneNumber"));
    ContactProjection expectedPreviousProjection = new ContactProjection(contactId, CREATED, null, emailAddress, null, null);

    ContactProjection latestProjection = new ContactProjection(contactId, eventStream);
    ContactProjection previousProjection = new ContactProjection(contactId, eventStream, now().plusMinutes(1));

    SoftAssertions.assertSoftly(softAssertions -> {
      softAssertions.assertThat(latestProjection)
          .usingRecursiveComparison()
          .ignoringFields("eventStream", "id")
          .isEqualTo(expectedLatestProjection);
      softAssertions.assertThat(previousProjection)
          .usingRecursiveComparison()
          .ignoringFields("eventStream", "id")
          .isEqualTo(expectedPreviousProjection);
    });
  }
}
