# JodaTimeCodecs

Provides TypeCodecs which provide converters for the DataStax Cassandra Java Driver (v2.2.0) and Joda Time.

## Usage

Register the codec with your cluster connection.

```java
// Setup the cluster connection
Cluster cluster = Cluster.builder().addContactPoint(args[0]).build();

// Configure our DateTimeCodec
CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
codecRegistry.register(new DateTimeCodec());
```

Insert data

```java
// Test writing a DateTime value
session.execute("INSERT INTO my_table (partition_key, some_timestamp) VALUES (?, ?)", "foo", DateTime.now());
```

Query data

```java
// Test retrieving a DateTime value
ResultSet results = session.execute("SELECT * FROM my_table WHERE partition_key = 'foo'");
for (Row row : results) {
  DateTime value = row.get("some_timestamp", DateTime.class);

  System.out.println(value);
  System.out.println(value.toLocalDate());
}
```

A full example may be found in `Exploration.java`.
