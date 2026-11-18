package com.sdet.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;

public class UserApiClient {
    private final APIRequestContext apiContext;
    private final ObjectMapper mapper = new ObjectMapper();

    public UserApiClient(APIRequestContext apiContext) {
        this.apiContext = apiContext;
    }

    public APIResponse getUserById(int userId) {
        return apiContext.get("/users/" + userId);
    }

    public String extractField(APIResponse response, String fieldName) {
        try {
            JsonNode root = mapper.readTree(response.text());
            return root.get(fieldName).asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
}