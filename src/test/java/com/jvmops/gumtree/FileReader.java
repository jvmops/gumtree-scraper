package com.jvmops.gumtree;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileReader {
    public static String toString(String path) {
        return toString(new ClassPathResource(path));
    }

    private static String toString(Resource resource) {
        try (var is = resource.getInputStream()) {
            return new Scanner(is, StandardCharsets.UTF_8.name())
                    .useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
