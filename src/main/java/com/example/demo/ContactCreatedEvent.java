package com.example.demo;

import lombok.Value;

import java.util.UUID;

@Value
public class ContactCreatedEvent implements INotifyDomainEvent {
  private UUID contactUuid;
}
