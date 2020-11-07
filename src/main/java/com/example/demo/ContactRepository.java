package com.example.demo;

import java.util.Optional;
import java.util.UUID;

public interface ContactRepository {
  void delete(UUID contactId);

  Optional<Contact> get(UUID id);
}
