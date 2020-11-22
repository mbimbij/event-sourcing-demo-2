package com.example.demo;

import lombok.Value;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class ContactChangedEmailAddressEvent extends INotifyDomainEvent {
  private Contact.EmailAddress newEmailAddress;

  public ContactChangedEmailAddressEvent(UUID uuid, Contact.EmailAddress newEmailAddress) {
    super(uuid);
    this.newEmailAddress = newEmailAddress;
  }

  public ContactChangedEmailAddressEvent(UUID contactId, ZonedDateTime eventDateTime, Contact.EmailAddress newEmailAddress) {
    super(contactId, eventDateTime);
    this.newEmailAddress = newEmailAddress;
  }
}
