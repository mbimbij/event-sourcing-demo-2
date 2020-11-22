package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static com.example.demo.Contact.State.CREATED;
import static com.example.demo.Contact.State.DELETED;

@Getter
@AllArgsConstructor
public class ContactProjection implements Observer {
  private UUID contactId;
  private Contact.State state;
  private Contact.Username username;
  private Contact.EmailAddress emailAddress;
  private Contact.Address address;
  private Contact.PhoneNumber phoneNumber;

  public ContactProjection(UUID contactId, EventStream eventStream) {
    this.contactId = contactId;
    eventStream.subscribe(this);
    eventStream.getEvents(contactId).forEach(this::apply);
  }

  public ContactProjection(UUID contactId, EventStream eventStream, ZonedDateTime atTime) {
    this.contactId = contactId;
    StreamSupport.stream(eventStream.getEvents(contactId).spliterator(),false)
        .filter(domainEvent -> domainEvent.getEventTime().isBefore(atTime))
        .forEach(this::apply);
  }

  @Override
  public UUID getObservedAggregate() {
    return contactId;
  }

  @Override
  public void doNotify(INotifyDomainEvent event) {
    apply(event);
  }

  private void apply(INotifyDomainEvent event) {
    if (event instanceof ContactCreatedEvent) {
      state = CREATED;
      username = ((ContactCreatedEvent) event).getUsername();
      emailAddress = ((ContactCreatedEvent) event).getEmailAddress();
      address = ((ContactCreatedEvent) event).getAddress();
      phoneNumber = ((ContactCreatedEvent) event).getPhoneNumber();
    } else if (event instanceof ContactChangedEmailAddressEvent) {
      emailAddress = ((ContactChangedEmailAddressEvent) event).getNewEmailAddress();
    } else if (event instanceof ContactChangedUsernameEvent) {
      username = ((ContactChangedUsernameEvent) event).getNewUsername();
    } else if (event instanceof ContactChangedAddressEvent) {
      address = ((ContactChangedAddressEvent) event).getNewAddress();
    } else if (event instanceof ContactChangedPhoneNumberEvent) {
      phoneNumber = ((ContactChangedPhoneNumberEvent) event).getNewPhoneNumber();
    } else if (event instanceof ContactDeletedEvent) {
      state = DELETED;
    }
  }

}
