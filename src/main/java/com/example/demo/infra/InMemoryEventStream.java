package com.example.demo.infra;

import com.example.demo.ContactCreatedEvent;
import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;
import com.example.demo.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryEventStream implements EventStream {
  private List<INotifyDomainEvent> events = new ArrayList<>();
  private List<Observer> observers = new ArrayList<>();

  @Override
  public Iterable<INotifyDomainEvent> getEvents() {
    return events;
  }

  @Override
  public void publish(INotifyDomainEvent event) {
    events.add(event);
    observers.stream()
        .filter(observer -> Objects.equals(observer.getObservedAggregate(), event.getAggregateId()))
        .forEach(observer -> observer.doNotify(event));
  }

  @Override
  public void subscribe(Observer observer) {
    observers.add(observer);
  }
}
