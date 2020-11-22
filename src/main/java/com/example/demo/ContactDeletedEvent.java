package com.example.demo;

import com.example.demo.INotifyDomainEvent;

import java.util.UUID;

public class ContactDeletedEvent extends INotifyDomainEvent {
  public ContactDeletedEvent(UUID aggregateId) {
    super(aggregateId);
  }
}
