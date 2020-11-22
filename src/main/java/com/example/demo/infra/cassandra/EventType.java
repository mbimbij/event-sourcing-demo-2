package com.example.demo.infra.cassandra;

import com.example.demo.Contact;

public enum EventType {
  CONTACT_CREATED, CONTACT_CHANGED_EMAIL, CONTACT_CHANGED_ADDRESS, CONTACT_CHANGED_PHONE_NUMBER, CONTACT_CHANGED_USERNAME, CONTACT_DELETED;
}
