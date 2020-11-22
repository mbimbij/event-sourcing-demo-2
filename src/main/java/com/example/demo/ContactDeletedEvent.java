package com.example.demo;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ContactDeletedEvent extends INotifyDomainEvent {
  public ContactDeletedEvent(UUID aggregateId) {
    super(aggregateId);
  }

  public ContactDeletedEvent(UUID contactId, ZonedDateTime eventDateTime) {
    super(contactId, eventDateTime);
  }
}
