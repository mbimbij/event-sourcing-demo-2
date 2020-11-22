package com.example.demo.infra.inmemory;

import com.example.demo.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.demo.Contact.State.CREATED;

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
    List<INotifyDomainEvent> events = StreamSupport.stream(eventStream.getEvents(id).spliterator(), false)
        .collect(Collectors.toList());
    Contact contact = new Contact(events);
    if(!Objects.equals(contact.getState(), CREATED)) {
      return Optional.empty();
    }else{
      return Optional.of(contact);
    }
  }
}
