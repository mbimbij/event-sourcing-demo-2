package com.example.demo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ContactChangedPhoneNumber extends INotifyDomainEvent {
  private Contact.PhoneNumber newPhoneNumber;

  public ContactChangedPhoneNumber(UUID id, Contact.PhoneNumber newPhoneNumber) {
    super(id);
    this.newPhoneNumber = newPhoneNumber;
  }
}
