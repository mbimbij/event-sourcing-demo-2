package com.example.demo.infra;

import com.example.demo.ContactCreatedEvent;
import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;
import com.example.demo.Observer;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStream implements EventStream {
  private List<INotifyDomainEvent> events = new ArrayList<>();
  private List<Observer> observers = new ArrayList<>();

  @Override
  public Iterable<INotifyDomainEvent> getEvents() {
    return events;
  }

  @Override
  public void publish(ContactCreatedEvent contactCreatedEvent) {
    events.add(contactCreatedEvent);
    observers.forEach(observer -> observer.doNotify(contactCreatedEvent));
  }

  @Override
  public void subscribe(Observer observer) {
    observers.add(observer);
  }
}
