package com.o19s.bradfordcp;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import org.joda.time.DateTime;

import java.nio.ByteBuffer;

/**
 * Created by cbradford on 12/19/15.
 */
public class DateTimeCodec extends TypeCodec<DateTime> {
  public DateTimeCodec() {
    super(DataType.timestamp(), DateTime.class);
  }

  @Override
  public DateTime parse(String value) {
    if (value == null || value.equals("NULL"))
      return null;

    try {
      return DateTime.parse(value);
    } catch (IllegalArgumentException iae) {
      throw new InvalidTypeException("Could not parse format: " + value, iae);
    }
  }

  @Override
  public String format(DateTime value) {
    if (value == null)
      return "NULL";

    return Long.toString(value.getMillis());
  }

  @Override
  public ByteBuffer serialize(DateTime value, ProtocolVersion protocolVersion) {
    return value == null ? null : BigintCodec.instance.serializeNoBoxing(value.getMillis(), protocolVersion);
  }

  @Override
  public DateTime deserialize(ByteBuffer bytes, ProtocolVersion protocolVersion) {
    return bytes == null || bytes.remaining() == 0 ? null: new DateTime(BigintCodec.instance.deserializeNoBoxing(bytes, protocolVersion));
  }
}
