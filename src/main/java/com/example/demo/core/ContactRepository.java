package com.example.demo.core;

import com.example.demo.core.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.demo.core.Contact.State.CREATED;

public class ContactRepository {
  EventStream eventStream;

  public ContactRepository(EventStream eventStream) {
    this.eventStream = eventStream;
  }

  public void delete(UUID contactId) {
    eventStream.publish(new ContactDeletedEvent(contactId));
  }

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
