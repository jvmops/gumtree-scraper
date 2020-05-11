package com.jvmops.gumtree;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonDataInitializer {
    protected static final String DUMPED_ADS = "json/ads.json";
    protected static final String ACCEPTANCE_TEST_ADS = "json/acceptance_test_ads.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected static void reloadReadOnlyAds(MongoTemplate mongoTemplate, String jsonFilePath) {
        mongoTemplate.getCollection("ad")
                .deleteMany(new Document());
        mongoTemplate.getCollection("ad")
                .insertMany(readDocuments(jsonFilePath));
    }

    private static List<Document> readDocuments(String jsonFilePath) {
        return readJsonArray(jsonFilePath).stream()
                .map(Document::parse)
                .collect(Collectors.toList());
    }

    private static List<String> readJsonArray(String jsonFilePath) {
        try {
            Resource jsonFile = new ClassPathResource(jsonFilePath);
            JsonNode jsonArray = OBJECT_MAPPER.readTree(jsonFile.getURL());
            return StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(JsonNode::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read JSON file from: %s", jsonFilePath), e);
        }
    }
}
