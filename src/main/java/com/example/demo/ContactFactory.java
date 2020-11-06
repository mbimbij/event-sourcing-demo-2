package com.example.demo;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ContactFactory {
  private final EventStream eventStream;

  public Contact createContact() {
    UUID uuid = UUID.randomUUID();
    Contact contact = new Contact(uuid);
    eventStream.publish(new ContactCreatedEvent(uuid));
    return contact;
  }

  public Contact createContact(UUID contactUuid) {
    Contact contact = new Contact(contactUuid);
    eventStream.publish(new ContactCreatedEvent(contactUuid));
    return contact;
  }
}
