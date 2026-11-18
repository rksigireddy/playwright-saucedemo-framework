package com.sdet.context;

import com.microsoft.playwright.APIResponse;
import com.sdet.api.UserApiClient;
import com.sdet.factory.PlaywrightFactory;
import com.sdet.pages.InventoryPage;
import com.sdet.pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

/**
 * DEPENDENCY INJECTION CONTEXT:
 * PicoContainer creates one instance of TestContext per scenario and injects it into Step classes.
 */
public class TestContext {
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private UserApiClient userApiClient;
    private APIResponse apiResponse;
    private final Map<String, Object> scenarioData = new HashMap<>();

    // Lazy initialization ensures each Page Object is created only once per scenario
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(PlaywrightFactory.getPage());
        }
        return loginPage;
    }

    public InventoryPage getInventoryPage() {
        if (inventoryPage == null) {
            inventoryPage = new InventoryPage(PlaywrightFactory.getPage());
        }
        return inventoryPage;
    }

    public UserApiClient getUserApiClient() {
        if (userApiClient == null) {
            userApiClient = new UserApiClient(PlaywrightFactory.getApiContext());
        }
        return userApiClient;
    }

    public APIResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(APIResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    public void setScenarioData(String key, Object value) {
        scenarioData.put(key, value);
    }

    public String getScenarioData(String key) {
        return String.valueOf(scenarioData.get(key));
    }
}