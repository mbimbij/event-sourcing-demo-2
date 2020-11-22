package com.example.demo.infra.cassandra;

import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;
import com.example.demo.Observer;

import java.util.UUID;

public class CassandraEventStream implements EventStream {

  @Override
  public Iterable<INotifyDomainEvent> getEvents(UUID uuid) {
    return null;
  }

  @Override
  public void publish(INotifyDomainEvent domainEvent) {

  }

  @Override
  public void subscribe(Observer observer) {

  }
}
