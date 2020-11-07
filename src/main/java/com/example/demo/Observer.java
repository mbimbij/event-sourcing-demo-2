package com.example.demo;

import java.util.UUID;

public interface Observer {
  UUID getObservedAggregate();
  void doNotify(INotifyDomainEvent event);
}
