package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = CassandraAutoConfiguration.class)
public class EventSourcingDemo2Application {

	public static void main(String[] args) {
		SpringApplication.run(EventSourcingDemo2Application.class, args);
	}

}
