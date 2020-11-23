package com.example.demo;

import com.example.demo.core.ContactFactory;
import com.example.demo.core.ContactRepository;
import com.example.demo.core.EventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration(exclude = CassandraAutoConfiguration.class)
public class EventSourcingDemo2Application {

	public static void main(String[] args) {
		SpringApplication.run(EventSourcingDemo2Application.class, args);
	}

	@Bean
	public ContactFactory contactFactory(EventStream eventStream){
		return new ContactFactory(eventStream);
	}

	@Bean
	public ContactRepository contactRepository(EventStream eventStream){
		return new ContactRepository(eventStream);
	}
}
