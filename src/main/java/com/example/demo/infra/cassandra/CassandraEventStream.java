package com.example.demo.infra.cassandra;

import com.example.demo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CassandraEventStream implements EventStream {
  private final CassandraEventRepository eventRepository;
  private List<Observer> observers = new ArrayList<>();

  @Override
  public Iterable<INotifyDomainEvent> getEvents(UUID contactId) {
    return eventRepository.findAllByContactId(contactId).stream().map(event -> {
      switch (event.getEventType()) {
        case CONTACT_CREATED:
          return new ContactCreatedEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC), new Contact.EmailAddress(event.getEmailAddress()));
        case CONTACT_CHANGED_ADDRESS:
          return new ContactChangedAddressEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC), new Contact.Address(event.getAddress()));
        case CONTACT_CHANGED_PHONE_NUMBER:
          return new ContactChangedPhoneNumberEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC), new Contact.PhoneNumber(event.getPhoneNumber()));
        case CONTACT_CHANGED_EMAIL:
          return new ContactChangedEmailAddressEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC), new Contact.EmailAddress(event.getEmailAddress()));
        case CONTACT_CHANGED_USERNAME:
          return new ContactChangedUsernameEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC), new Contact.Username(event.getUsername()));
        case CONTACT_DELETED:
          return new ContactDeletedEvent(event.getContactId(), ZonedDateTime.ofLocal(event.getEventDateTime(), ZoneId.systemDefault(), ZoneOffset.UTC));
        default:
          throw new IllegalStateException("unknown event type: " + event.getEventType().name());
      }
    }).collect(Collectors.toList());
  }

  @Override
  public void publish(INotifyDomainEvent domainEvent) {
    if (domainEvent instanceof ContactCreatedEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CREATED,
          null,
          ((ContactCreatedEvent) domainEvent).getEmailAddress().getValue(),
          null,
          null);
      eventRepository.save(event);
    } else if (domainEvent instanceof ContactChangedUsernameEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CHANGED_USERNAME,
          ((ContactChangedUsernameEvent) domainEvent).getNewUsername().getValue(),
          null,
          null,
          null);
      eventRepository.save(event);
    } else if (domainEvent instanceof ContactChangedEmailAddressEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CHANGED_EMAIL,
          null,
          ((ContactChangedEmailAddressEvent) domainEvent).getNewEmailAddress().getValue(),
          null,
          null);
      eventRepository.save(event);
    } else if (domainEvent instanceof ContactChangedAddressEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CHANGED_ADDRESS,
          null,
          null,
          ((ContactChangedAddressEvent) domainEvent).getNewAddress().getValue(),
          null);
      eventRepository.save(event);
    } else if (domainEvent instanceof ContactChangedPhoneNumberEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CHANGED_PHONE_NUMBER,
          null,
          null,
          null,
          ((ContactChangedPhoneNumberEvent) domainEvent).getNewPhoneNumber().getValue());
      eventRepository.save(event);
    } else if (domainEvent instanceof ContactDeletedEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_DELETED,
          null,
          null,
          null,
          null);
      eventRepository.save(event);
    }
    observers.forEach(observer -> observer.doNotify(domainEvent));
  }

  @Override
  public void subscribe(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void unsubscribe(Observer observer) {
    observers.remove(observer);
  }
}
