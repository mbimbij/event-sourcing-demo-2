package com.example.demo.infra.cassandra;

import com.example.demo.core.EventStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "app.eventstore.implementation", havingValue = "cassandra")
public class AppCassandraConfiguration extends AbstractCassandraConfiguration {
  @Autowired
  CassandraProperties cassandraProperties;

  @Override
  protected String getContactPoints() {
    return String.join(",", cassandraProperties.getContactPoints());
  }

  @Override
  protected String getKeyspaceName() {
    return cassandraProperties.getKeyspaceName();
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.valueOf(cassandraProperties.getSchemaAction());
  }

  @Override
  protected String getLocalDataCenter() {
    return cassandraProperties.getLocalDatacenter();
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    return Collections.singletonList(CreateKeyspaceSpecification
        .createKeyspace(cassandraProperties.getKeyspaceName())
        .ifNotExists(true)
        .with(KeyspaceOption.DURABLE_WRITES, true)
        .withSimpleReplication());
  }

  @Bean
  public EventStream cassandraEventStream(CassandraEventRepository eventRepository){
    return new CassandraEventStream(eventRepository);
  }
}
