Create a topic with 3 partitions
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-topics --bootstrap-server kafka:9092 --create --topic contact-events --partitions 3 --replication-factor 1
```

Get topic information
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-topics --bootstrap-server kafka:9092 --describe --topic contact-events
```

Delete a topic
```
docker exec -it word-count-kafka-streams-cqrs_kafka_1 kafka-topics --bootstrap-server kafka:9092 --delete --topic contact-events
```