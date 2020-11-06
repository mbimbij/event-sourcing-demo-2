package com.example.demo;

public interface EventStream {
  Iterable<INotifyDomainEvent> getEvents();

  void publish(ContactCreatedEvent contactCreatedEvent);

  void subscribe(Observer observer);
}
