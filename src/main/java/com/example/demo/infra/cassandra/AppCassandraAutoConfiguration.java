package com.example.demo.infra.cassandra;

import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.eventstore.implementation", havingValue = "cassandra")
public class AppCassandraAutoConfiguration extends CassandraAutoConfiguration {}
