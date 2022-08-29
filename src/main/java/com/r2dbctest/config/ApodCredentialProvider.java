package com.r2dbctest.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ApodCredentialProvider {
    private final String path = System.getenv("HOME")+"/.apod/config";

    public String getKey() throws FileNotFoundException {
        Properties properties = new Properties();

        try(FileInputStream ipStream = new FileInputStream(path);) {
            properties.load(ipStream);
        } catch (IOException e) {
            throw new FileNotFoundException("failed to load your key");
        }

        if (properties.containsKey("api_key")) {
            return properties.getProperty("api_key");
        } else {
            throw new RuntimeException("visit the APOD website to get your first access key!");
        }
    }
}
