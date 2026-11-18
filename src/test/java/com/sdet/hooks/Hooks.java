package com.sdet.hooks;

import com.sdet.config.ConfigReader;
import com.sdet.factory.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class Hooks {

    @Before("@api or @hybrid")
    public void setUpApi() {
        String apiUrl = ConfigReader.getInstance().getProperty("api.url");
        PlaywrightFactory.initApi(apiUrl);
    }

    @Before("@ui or @hybrid")
    public void setUpBrowser() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getProperty("browser");
        boolean headless = Boolean.parseBoolean(config.getProperty("headless"));
        double slowMo = Double.parseDouble(config.getProperty("slow.mo"));

        PlaywrightFactory.initBrowser(browser, headless, slowMo);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && PlaywrightFactory.getPage() != null) {
            byte[] screenshot = PlaywrightFactory.getPage().screenshot();
            Allure.addAttachment("Failure Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
        }
        PlaywrightFactory.closeAll();
    }
}