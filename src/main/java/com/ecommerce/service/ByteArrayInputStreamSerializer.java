package com.ecommerce.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ByteArrayInputStreamSerializer extends JsonSerializer<ByteArrayInputStream> {

    @Override
    public void serialize(ByteArrayInputStream value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        byte[] buffer = new byte[value.available()];
        value.read(buffer);
        gen.writeBinary(buffer);
    }
}
