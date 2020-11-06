package com.example.demo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ContactProjection implements Observer {
  private UUID contactUuid;
  private boolean created;

  public ContactProjection(UUID contactUuid, EventStream eventStream) {
    this.contactUuid = contactUuid;
    eventStream.subscribe(this);
  }

  @Override
  public void doNotify(INotifyDomainEvent event) {
    if(event instanceof ContactCreatedEvent){
      created = true;
    }
  }
}
