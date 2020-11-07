package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ContactProjection implements Observer {
  private UUID contactUuid;
  private boolean created;
  private Contact.Username username;
  private Contact.EmailAddress emailAddress;
  private Contact.Address address;
  private Contact.PhoneNumber phoneNumber;

  public ContactProjection(UUID contactUuid, EventStream eventStream) {
    this.contactUuid = contactUuid;
    eventStream.subscribe(this);
    eventStream.getEvents().forEach(this::apply);
  }

  @Override
  public UUID getObservedAggregate() {
    return contactUuid;
  }

  @Override
  public void doNotify(INotifyDomainEvent event) {
    apply(event);
  }

  private void apply(INotifyDomainEvent event) {
    if (event instanceof ContactCreatedEvent) {
      created = true;
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
    } else if (event instanceof ContactChangedPhoneNumber) {
      phoneNumber = ((ContactChangedPhoneNumber) event).getNewPhoneNumber();
    }
  }

}
