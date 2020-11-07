package com.example.demo.infra;

import com.example.demo.Contact;
import com.example.demo.ContactRepository;
import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.demo.Contact.State.CREATED;
import static com.example.demo.Contact.State.DELETED;

public class InMemoryContactRepository implements ContactRepository {
  EventStream eventStream;

  public InMemoryContactRepository(EventStream eventStream) {
    this.eventStream = eventStream;
  }

  @Override
  public void delete(UUID contactId) {
    eventStream.publish(new ContactDeletedEvent(contactId));
  }

  @Override
  public Optional<Contact> get(UUID id) {
    List<INotifyDomainEvent> events = StreamSupport.stream(eventStream.getEvents().spliterator(), false)
        .filter(domainEvent -> Objects.equals(domainEvent.getAggregateId(), id)).collect(Collectors.toList());
    Contact contact = new Contact(events);
    if(!Objects.equals(contact.getState(), CREATED)) {
      return Optional.empty();
    }else{
      return Optional.of(contact);
    }
  }

  @Override
  public Optional<Contact> getAtTime(UUID id, ZonedDateTime atTime) {
    List<INotifyDomainEvent> events = StreamSupport.stream(eventStream.getEvents().spliterator(), false)
        .filter(domainEvent -> domainEvent.getEventTime().isBefore(atTime))
        .filter(domainEvent -> Objects.equals(domainEvent.getAggregateId(), id)).collect(Collectors.toList());
    Contact contact = new Contact(events);
    if(!Objects.equals(contact.getState(), CREATED)) {
      return Optional.empty();
    }else{
      return Optional.of(contact);
    }
  }
}
