package com.sdet.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class InventoryPage {
    private final Page page;

    private final Locator pageTitle;
    private final Locator shoppingCartBadge;

    public InventoryPage(Page page) {
        this.page = page;
        this.pageTitle = page.locator(".title");
        this.shoppingCartBadge = page.locator(".shopping_cart_badge");
    }

    public Locator getPageTitle() {
        return pageTitle;
    }

    public void addProductToCart(String productName) {
        Locator productCard = page.locator(".inventory_item").filter(
                new Locator.FilterOptions().setHasText(productName)
        );
        productCard.locator("button").click();
    }

    public Locator getCartBadge() {
        return shoppingCartBadge;
    }
}