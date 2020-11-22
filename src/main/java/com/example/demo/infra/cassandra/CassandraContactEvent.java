package com.example.demo.infra.cassandra;

import com.example.demo.Contact;
import lombok.ToString;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("contact_events")
@Value
public class CassandraContactEvent {
  @PrimaryKey
  private CassandraContactEventKey key;
  @Column("event_type")
  private EventType eventType;
  @Column("username")
  private String username;
  @Column("email_address")
  private String emailAddress;
  @Column("address")
  private String address;
  @Column("phone_number")
  private String phoneNumber;
}
