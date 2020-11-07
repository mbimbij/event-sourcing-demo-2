package com.example.demo;

public interface EventStream {
  Iterable<INotifyDomainEvent> getEvents();

  void publish(INotifyDomainEvent domainEvent);

  void subscribe(Observer observer);
}
