package com.example.demo;

public interface Observer {
  void doNotify(INotifyDomainEvent event);
}
