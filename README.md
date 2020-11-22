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

kafkacat (be mindful of the network the kafka container is connected to)

```
docker run --network=word-count-kafka-streams-cqrs_default edenhill/kafkacat:1.6.0   kafkacat -b PLAINTEXT://kafka:9092 -t contact-events -C  -f '\nKey (%K bytes): %k Partition: %p\n'
```