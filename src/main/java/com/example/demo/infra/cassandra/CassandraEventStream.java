package com.example.demo.infra.cassandra;

import com.example.demo.ContactCreatedEvent;
import com.example.demo.EventStream;
import com.example.demo.INotifyDomainEvent;
import com.example.demo.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CassandraEventStream implements EventStream {
  @Autowired
  private CassandraEventRepository eventRepository;

  @Override
  public Iterable<INotifyDomainEvent> getEvents(UUID uuid) {
    return null;
  }

  @Override
  public void publish(INotifyDomainEvent domainEvent) {
    if (domainEvent instanceof ContactCreatedEvent) {
      UUID aggregateId = domainEvent.getAggregateId();
      CassandraContactEvent event = new CassandraContactEvent(aggregateId,
          domainEvent.getEventTime().toLocalDateTime(),
          EventType.CONTACT_CREATED,
          null,
          ((ContactCreatedEvent) domainEvent).getEmailAddress().getValue(),
          null,
          null);
      eventRepository.save(event);
    }
  }

  @Override
  public void subscribe(Observer observer) {

  }
}
