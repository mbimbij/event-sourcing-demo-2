package com.example.demo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Contact {
  private final UUID uuid;
}
