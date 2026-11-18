package com.sdet.steps;

import com.microsoft.playwright.APIResponse;
import com.sdet.config.ConfigReader;
import com.sdet.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SauceDemoSteps {

    private final TestContext context;

    // PicoContainer automatically injects TestContext here
    public SauceDemoSteps(TestContext context) {
        this.context = context;
    }

    // ================= UI STEPS =================

    @Given("the user navigates to the SauceDemo login page")
    public void userNavigatesToSauceDemo() {
        String uiUrl = ConfigReader.getInstance().getProperty("ui.url");
        context.getLoginPage().openSauceDemo(uiUrl);
    }

    @When("the user logs in with username {string} and password {string}")
    public void userLogsIn(String username, String password) {
        context.getLoginPage().login(username, password);
    }

    @Then("the user should be redirected to the {string} inventory page")
    public void verifyInventoryPageTitle(String expectedTitle) {
        assertThat(context.getInventoryPage().getPageTitle()).hasText(expectedTitle);
    }

    @When("the user adds {string} to the shopping cart")
    public void userAddsProductToCart(String productName) {
        context.getInventoryPage().addProductToCart(productName);
    }

    @Then("the shopping cart badge should display {string}")
    public void verifyCartBadgeCount(String expectedCount) {
        assertThat(context.getInventoryPage().getCartBadge()).hasText(expectedCount);
    }

    // ================= API STEPS =================

    @Given("the client requests details for user ID {int} via API")
    public void requestUserDetailsViaApi(int userId) {
        APIResponse response = context.getUserApiClient().getUserById(userId);
        context.setApiResponse(response);

        // Save the extracted API username into TestContext so UI steps can use it in @hybrid tests
        String apiUsername = context.getUserApiClient().extractField(response, "username");
        context.setScenarioData("API_USERNAME", apiUsername);
    }

    @Then("the API response status code should be {int}")
    public void verifyApiStatusCode(int expectedStatusCode) {
        Assertions.assertEquals(expectedStatusCode, context.getApiResponse().status());
    }

    @Then("the API response username should be {string}")
    public void verifyApiUsername(String expectedUsername) {
        Assertions.assertEquals(expectedUsername, context.getScenarioData("API_USERNAME"));
    }

    // ================= HYBRID (API -> UI) STEPS =================

    @When("the user attempts to log into SauceDemo using the fetched API username and password {string}")
    public void userLogsInWithApiUsername(String password) {
        String fetchedApiUser = context.getScenarioData("API_USERNAME");
        context.getLoginPage().login(fetchedApiUser, password);
    }

    @Then("the login error message should contain {string}")
    public void verifyLoginErrorMessage(String expectedErrorSubstring) {
        assertThat(context.getLoginPage().getErrorMessage()).containsText(expectedErrorSubstring);
    }
}