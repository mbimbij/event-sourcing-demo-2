package com.example.demo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ContactChangedAddressEvent extends INotifyDomainEvent {
  private Contact.Address newAddress;

  public ContactChangedAddressEvent(UUID id, Contact.Address newAddress) {
    super(id);
    this.newAddress = newAddress;
  }
}
