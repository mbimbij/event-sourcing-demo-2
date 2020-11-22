package com.example.demo;

import java.util.UUID;

public interface EventStream {

  Iterable<INotifyDomainEvent> getEvents(UUID uuid);

  void publish(INotifyDomainEvent domainEvent);

  void subscribe(Observer observer);
}
