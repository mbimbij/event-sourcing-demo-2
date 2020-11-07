package com.example.demo.infra;

import com.example.demo.INotifyDomainEvent;

import java.util.UUID;

public class ContactDeletedEvent extends INotifyDomainEvent {
  public ContactDeletedEvent(UUID aggregateId) {
    super(aggregateId);
  }
}
