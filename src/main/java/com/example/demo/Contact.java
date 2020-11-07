package com.example.demo;

import com.example.demo.infra.ContactDeletedEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import static com.example.demo.Contact.State.*;

@Getter
public class Contact {
  private final UUID id = UUID.randomUUID();
  private State state = NOT_CREATED;
  private Username username;
  private EmailAddress emailAddress;
  private Address address;
  private PhoneNumber phoneNumber;
  private EventStream eventStream;

  public Contact(EmailAddress emailAddress, EventStream eventStream) {
    this.emailAddress = emailAddress;
    this.eventStream = eventStream;
  }

  public Contact(State state, Username username, EmailAddress emailAddress, Address address, PhoneNumber phoneNumber, EventStream eventStream) {
    this.state = state;
    this.username = username;
    this.emailAddress = emailAddress;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.eventStream = eventStream;
  }

  public Contact(List<INotifyDomainEvent> events) {
    events.forEach(this::apply);
  }

  private void apply(INotifyDomainEvent event) {
    if (event instanceof ContactCreatedEvent) {
      state = CREATED;
      username = ((ContactCreatedEvent) event).getUsername();
      emailAddress = ((ContactCreatedEvent) event).getEmailAddress();
      address = ((ContactCreatedEvent) event).getAddress();
      phoneNumber = ((ContactCreatedEvent) event).getPhoneNumber();
    } else if (event instanceof ContactChangedEmailAddressEvent) {
      emailAddress = ((ContactChangedEmailAddressEvent) event).getNewEmailAddress();
    } else if (event instanceof ContactChangedUsernameEvent) {
      username = ((ContactChangedUsernameEvent) event).getNewUsername();
    } else if (event instanceof ContactChangedAddressEvent) {
      address = ((ContactChangedAddressEvent) event).getNewAddress();
    } else if (event instanceof ContactChangedPhoneNumberEvent) {
      phoneNumber = ((ContactChangedPhoneNumberEvent) event).getNewPhoneNumber();
    } else if (event instanceof ContactDeletedEvent) {
      state = DELETED;
    }
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
    eventStream.publish(new ContactChangedPhoneNumberEvent(id, newPhoneNumber));
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

  public static enum State {
    NOT_CREATED, CREATED, DELETED
  }
}
