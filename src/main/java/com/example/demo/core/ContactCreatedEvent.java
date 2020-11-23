package com.example.demo.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class ContactCreatedEvent extends INotifyDomainEvent {
  private Contact.Username username;
  private Contact.EmailAddress emailAddress;
  private Contact.Address address;
  private Contact.PhoneNumber phoneNumber;

  public ContactCreatedEvent(UUID contactUuid) {
    super(contactUuid);
  }

  public ContactCreatedEvent(UUID contactUuid, Contact.EmailAddress emailAddress) {
    super(contactUuid);
    this.emailAddress = emailAddress;
  }

  public ContactCreatedEvent(UUID aggregateId, ZonedDateTime eventTime) {
    super(aggregateId, eventTime);
  }

  public ContactCreatedEvent(UUID aggregateId, ZonedDateTime eventTime, Contact.EmailAddress emailAddress) {
    super(aggregateId, eventTime);
    this.emailAddress = emailAddress;
  }
}
