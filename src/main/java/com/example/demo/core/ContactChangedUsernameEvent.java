package com.example.demo.core;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class ContactChangedUsernameEvent extends INotifyDomainEvent {
  private Contact.Username newUsername;

  public ContactChangedUsernameEvent(UUID aggregateId, Contact.Username newUsername) {
    super(aggregateId);
    this.newUsername = newUsername;
  }

  public ContactChangedUsernameEvent(UUID contactId, ZonedDateTime eventDateTime, Contact.Username newUsername) {
    super(contactId, eventDateTime);
    this.newUsername = newUsername;
  }
}
