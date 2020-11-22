package com.example.demo.infra.cassandra;

import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.ASCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Table("contact_events")
@Value
public class CassandraContactEvent {
  @PrimaryKeyColumn(name = "contact_id", type = PARTITIONED)
  private UUID contactId;
  @PrimaryKeyColumn(name = "event_date_time", ordinal = 1, ordering = ASCENDING)
  private LocalDateTime eventDateTime;
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
