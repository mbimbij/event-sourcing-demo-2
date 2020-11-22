package com.example.demo.infra.inmemory;

import com.example.demo.ContactCreatedEvent;
import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;
import com.example.demo.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class InMemoryEventStream implements EventStream {
  private List<INotifyDomainEvent> events = new ArrayList<>();
  private List<Observer> observers = new ArrayList<>();

  public Iterable<INotifyDomainEvent> getEvents() {
    return events;
  }

  @Override
  public Iterable<INotifyDomainEvent> getEvents(UUID aggregateId) {
    return events.stream().filter(domainEvent -> Objects.equals(domainEvent.getAggregateId(), aggregateId)).collect(Collectors.toList());
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
