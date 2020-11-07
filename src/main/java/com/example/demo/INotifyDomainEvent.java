package com.example.demo;

import java.util.UUID;

public abstract class INotifyDomainEvent {
  protected UUID aggregateId;

  public INotifyDomainEvent(UUID aggregateId) {
    this.aggregateId = aggregateId;
  }

  public UUID getAggregateId() {
    return aggregateId;
  }
}
