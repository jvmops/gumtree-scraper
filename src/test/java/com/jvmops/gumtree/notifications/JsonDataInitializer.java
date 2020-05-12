package com.jvmops.gumtree.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonDataInitializer {
    protected static final ClassPathResource DUMPED_ADS = new ClassPathResource("json/ads.json");
    protected static final ClassPathResource ACCEPTANCE_TEST_ADS = new ClassPathResource("json/acceptance_test_ads.json");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected static void reload(MongoTemplate mongoTemplate, ClassPathResource jsonFile) {
        mongoTemplate.getCollection("ad")
                .deleteMany(new Document());
        mongoTemplate.getCollection("ad")
                .insertMany(parse(jsonFile));
    }

    private static List<Document> parse(ClassPathResource jsonFile) {
        return readJsonArray(jsonFile).stream()
                .map(Document::parse)
                .collect(Collectors.toList());
    }

    private static List<String> readJsonArray(ClassPathResource jsonFile) {
        try {
            JsonNode jsonArray = OBJECT_MAPPER.readTree(jsonFile.getURL());
            return StreamSupport.stream(jsonArray.spliterator(), false)
                    .map(JsonNode::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to read JSON file from: %s", jsonFile), e);
        }
    }
}
