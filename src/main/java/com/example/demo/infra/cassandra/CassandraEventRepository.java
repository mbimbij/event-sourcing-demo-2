package com.example.demo.infra.cassandra;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "app.eventstore.implementation", havingValue = "cassandra")
public interface CassandraEventRepository extends CassandraRepository<CassandraContactEvent, UUID> {
  List<CassandraContactEvent> findAllByContactId(UUID contactId);
}
