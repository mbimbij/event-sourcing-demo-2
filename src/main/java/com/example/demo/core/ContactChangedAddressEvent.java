package com.example.demo.core;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
public class ContactChangedAddressEvent extends INotifyDomainEvent {
  private Contact.Address newAddress;

  public ContactChangedAddressEvent(UUID id, Contact.Address newAddress) {
    super(id);
    this.newAddress = newAddress;
  }

  public ContactChangedAddressEvent(UUID contactId2, ZonedDateTime eventDateTime, Contact.Address newAddress) {
    super(contactId2, eventDateTime);
    this.newAddress = newAddress;
  }
}
