package com.example.demo.core;

import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public abstract class INotifyDomainEvent {
  protected UUID aggregateId;
  protected ZonedDateTime eventTime;

  protected INotifyDomainEvent(UUID aggregateId) {
    this.aggregateId = aggregateId;
    this.eventTime = now();
  }

  protected INotifyDomainEvent(UUID aggregateId, ZonedDateTime eventTime) {
    this.aggregateId = aggregateId;
    this.eventTime = eventTime;
  }

  public UUID getAggregateId() {
    return aggregateId;
  }

  public ZonedDateTime getEventTime() {
    return eventTime;
  }
}
