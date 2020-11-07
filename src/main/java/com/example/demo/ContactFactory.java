package com.example.demo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContactFactory {
  private final EventStream eventStream;

  public Contact createContact(Contact.EmailAddress emailAddress) {
    Contact contact = new Contact(emailAddress, eventStream);
    eventStream.publish(new ContactCreatedEvent(contact.getId(), emailAddress));
    return contact;
  }
}
