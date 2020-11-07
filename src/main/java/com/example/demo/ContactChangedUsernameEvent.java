package com.example.demo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ContactChangedUsernameEvent extends INotifyDomainEvent {
  private Contact.Username newUsername;

  public ContactChangedUsernameEvent(UUID aggregateId, Contact.Username newUsername) {
    super(aggregateId);
    this.newUsername = newUsername;
  }
}
