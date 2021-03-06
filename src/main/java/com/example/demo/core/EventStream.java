package com.example.demo.core;

import java.util.UUID;

public interface EventStream {

  Iterable<INotifyDomainEvent> getEvents(UUID uuid);

  void publish(INotifyDomainEvent domainEvent);

  void subscribe(Observer observer);

  void unsubscribe(Observer observer);
}
