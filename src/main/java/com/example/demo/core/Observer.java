package com.example.demo.core;

import java.util.UUID;

public interface Observer {
  UUID getObservedAggregate();
  void doNotify(INotifyDomainEvent event);
}
