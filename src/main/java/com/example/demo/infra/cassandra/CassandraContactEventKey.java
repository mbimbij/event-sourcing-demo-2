package com.example.demo.infra.cassandra;

import lombok.Value;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.ASCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@PrimaryKeyClass
@Value
public class CassandraContactEventKey implements Serializable {
  @PrimaryKeyColumn(name = "contact_id", type = PARTITIONED)
  private UUID contactId;

  @PrimaryKeyColumn(name = "event_date_time", ordinal = 1, ordering = ASCENDING)
  private LocalDateTime eventDateTime;
}
