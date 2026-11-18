Feature: SauceDemo E-Commerce UI, API, and Hybrid Verification

  @ui
  Scenario: 1. Verify standard user can login and add a backpack to the cart (UI Only)
    Given the user navigates to the SauceDemo login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the user should be redirected to the "Products" inventory page
    When the user adds "Sauce Labs Backpack" to the shopping cart
    Then the shopping cart badge should display "1"

  @api
  Scenario: 2. Verify user details endpoint returns valid data (API Only - No Browser Opens)
    Given the client requests details for user ID 1 via API
    Then the API response status code should be 200
    And the API response username should be "Bret"

  @hybrid
  Scenario: 3. Fetch username from API and verify SauceDemo rejects unregistered API user (Hybrid API + UI)
    Given the client requests details for user ID 1 via API
    Then the API response status code should be 200
    Given the user navigates to the SauceDemo login page
    When the user attempts to log into SauceDemo using the fetched API username and password "secret_sauce"
    Then the login error message should contain "Username and password do not match"