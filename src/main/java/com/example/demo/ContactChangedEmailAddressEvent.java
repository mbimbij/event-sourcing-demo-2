package com.example.demo;

import lombok.Value;

import java.util.UUID;

@Value
public class ContactChangedEmailAddressEvent extends INotifyDomainEvent {
  private Contact.EmailAddress newEmailAddress;

  public ContactChangedEmailAddressEvent(UUID uuid, Contact.EmailAddress newEmailAddress) {
    super(uuid);
    this.newEmailAddress = newEmailAddress;
  }
}
