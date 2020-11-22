package com.example.demo.infra.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CassandraEventRepository extends CassandraRepository<CassandraContactEvent, UUID> {
  List<CassandraContactEvent> findAllByContactId(UUID contactId);
}
