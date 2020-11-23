package com.example.demo.core;

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
