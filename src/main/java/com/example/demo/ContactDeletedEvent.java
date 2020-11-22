package com.example.demo;

import java.util.UUID;

public class ContactDeletedEvent extends INotifyDomainEvent {
  public ContactDeletedEvent(UUID aggregateId) {
    super(aggregateId);
  }
}
