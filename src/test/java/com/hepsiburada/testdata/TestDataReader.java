package com.hepsiburada.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TestDataReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SearchTestData readSearchTestData(String classpathResource) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IllegalStateException("Test data resource not found on classpath: " + classpathResource);
            }
            return objectMapper.readValue(in, SearchTestData.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read test data resource: " + classpathResource, e);
        }
    }
}
