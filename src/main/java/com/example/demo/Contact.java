package com.example.demo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Contact {
  private final UUID id = UUID.randomUUID();
  private Username username;
  private EmailAddress emailAddress;
  private Address address;
  private PhoneNumber phoneNumber;
  private EventStream eventStream;

  public Contact(EmailAddress emailAddress, EventStream eventStream) {
    this.emailAddress = emailAddress;
    this.eventStream = eventStream;
  }

  public Contact(Username username, EmailAddress emailAddress, Address address, PhoneNumber phoneNumber, EventStream eventStream) {
    this.username = username;
    this.emailAddress = emailAddress;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.eventStream = eventStream;
  }

  public void setEmailAddress(EmailAddress newEmailAddress) {
    this.emailAddress = newEmailAddress;
    eventStream.publish(new ContactChangedEmailAddressEvent(id, newEmailAddress));
  }

  public void setUsername(Username newUsername) {
    this.username = newUsername;
    eventStream.publish(new ContactChangedUsernameEvent(id, newUsername));
  }

  public void setAddress(Address newAddress) {
    this.address = newAddress;
    eventStream.publish(new ContactChangedAddressEvent(id, newAddress));
  }

  public void setPhoneNumber(PhoneNumber newPhoneNumber) {
    this.phoneNumber = newPhoneNumber;
    eventStream.publish(new ContactChangedPhoneNumber(id, newPhoneNumber));
  }

  public static class Username extends ValueObject<String> {
    public Username(String value) {
      super(value);
    }
  }

  public static class EmailAddress extends ValueObject<String> {
    public EmailAddress(String value) {
      super(value);
    }
  }

  public static class Address extends ValueObject<String> {
    public Address(String value) {
      super(value);
    }
  }

  public static class PhoneNumber extends ValueObject<String> {
    public PhoneNumber(String value) {
      super(value);
    }
  }
}
