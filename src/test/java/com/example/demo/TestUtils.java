package com.example.demo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class TestUtils {
  public static final Comparator<ZonedDateTime> ZONED_DATE_TIME_COMPARATOR = (dateTime, t1) -> (dateTime.toInstant().toEpochMilli() == t1.toInstant().toEpochMilli()) ? 0 : 1;
  public static final Comparator<LocalDateTime> LOCAL_DATE_TIME_COMPARATOR = (dateTime, t1) -> (dateTime.toInstant(ZoneOffset.UTC).toEpochMilli() == t1.toInstant(ZoneOffset.UTC).toEpochMilli()) ? 0 : 1;
}
