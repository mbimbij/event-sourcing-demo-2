package com.example.demo.infra.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

@Configuration
public class AppCassandraConfiguration extends AbstractCassandraConfiguration {
  @Autowired
  CassandraProperties appCassandraConfiguration;

  @Override
  protected String getContactPoints() {
    return String.join(",", appCassandraConfiguration.getContactPoints());
  }

  @Override
  protected String getKeyspaceName() {
    return appCassandraConfiguration.getKeyspaceName();
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.valueOf(appCassandraConfiguration.getSchemaAction());
  }

  @Override
  protected String getLocalDataCenter() {
    return appCassandraConfiguration.getLocalDatacenter();
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    return Collections.singletonList(CreateKeyspaceSpecification
        .createKeyspace(appCassandraConfiguration.getKeyspaceName())
        .ifNotExists(true)
        .with(KeyspaceOption.DURABLE_WRITES, true)
        .withSimpleReplication());
  }

}
