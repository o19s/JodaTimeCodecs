package com.o19s.bradfordcp;

import com.datastax.driver.core.*;
import org.joda.time.DateTime;

/**
 * Created by cbradford on 12/19/15.
 */
public class Exploration {
  public static void main(String[] args) {
    // Setup the cluster connection
    Cluster cluster = Cluster.builder().addContactPoint(args[0]).build();
    Session session = cluster.connect("jodaplayground");

    // Configure our DateTimeCodec
    CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
    codecRegistry.register(new DateTimeCodec());

    try {
      // Test writing a DateTime value
      session.execute("INSERT INTO my_table (partition_key, some_timestamp) VALUES (?, ?)", "foo", DateTime.now());

      // Test retrieving a DateTime value
      ResultSet results = session.execute("SELECT * FROM my_table WHERE partition_key = 'foo'");
      for (Row row : results) {
        DateTime value = row.get("some_timestamp", DateTime.class);

        System.out.println(value);
        System.out.println(value.toLocalDate());
      }
    } finally {
      session.close();
      cluster.close();
    }
  }
}
