package com.hepsiburada.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Element seçicileri (ve websiteURL gibi düz config değerleri) buradan, tek bir JSON
// dosyasından okunur. Site DOM'u değiştiğinde sadece config/elements.json güncellenir,
// Java koduna dokunmaya gerek kalmaz.
//
// USERNAME/PASSWORD gibi gizli değerler ayrı, git'e commit'lenmeyen config/credentials.json
// dosyasından okunup aynı anahtar uzayına eklenir (bkz. config/credentials.example.json).
public final class ElementRepository {

    private static final String RESOURCE = "config/elements.json";
    private static final String CREDENTIALS_RESOURCE = "config/credentials.json";
    private static final Map<String, ElementDefinition> ELEMENTS = load();

    private ElementRepository() {
    }

    public static String value(String key) {
        return require(key).getValue();
    }

    public static By locator(String key) {
        ElementDefinition definition = require(key);
        String type = definition.getType() == null ? "css" : definition.getType();
        String value = definition.getValue();
        return switch (type) {
            case "id" -> By.id(value);
            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "name" -> By.name(value);
            case "tag" -> By.tagName(value);
            default -> throw new IllegalArgumentException(
                "Unsupported locator type '" + type + "' for key '" + key + "'");
        };
    }

    private static ElementDefinition require(String key) {
        ElementDefinition definition = ELEMENTS.get(key);
        if (definition == null) {
            throw new IllegalStateException("No element definition found for key: " + key
                + " (config/elements.json veya config/credentials.json içinde tanımlı mı kontrol edin)");
        }
        return definition;
    }

    private static Map<String, ElementDefinition> load() {
        Map<String, ElementDefinition> definitions = new HashMap<>(readDefinitions(RESOURCE, true));
        definitions.putAll(readDefinitions(CREDENTIALS_RESOURCE, false));
        return definitions;
    }

    private static Map<String, ElementDefinition> readDefinitions(String resource, boolean required) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream in = ElementRepository.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                if (required) {
                    throw new IllegalStateException("Element repository resource not found on classpath: " + resource);
                }
                return Map.of();
            }
            List<ElementDefinition> definitions = objectMapper.readValue(
                in, objectMapper.getTypeFactory().constructCollectionType(List.class, ElementDefinition.class));
            return definitions.stream().collect(Collectors.toMap(ElementDefinition::getKey, d -> d));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read element repository resource: " + resource, e);
        }
    }
}
