package com.example.demo.core;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class ContactChangedPhoneNumberEvent extends INotifyDomainEvent {
  private Contact.PhoneNumber newPhoneNumber;

  public ContactChangedPhoneNumberEvent(UUID id, Contact.PhoneNumber newPhoneNumber) {
    super(id);
    this.newPhoneNumber = newPhoneNumber;
  }

  public ContactChangedPhoneNumberEvent(UUID aggregateId, ZonedDateTime eventTime, Contact.PhoneNumber newPhoneNumber) {
    super(aggregateId, eventTime);
    this.newPhoneNumber = newPhoneNumber;
  }
}
