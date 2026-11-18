package com.sdet.factory;

import com.microsoft.playwright.*;

/**
 * FACTORY PATTERN + THREAD-LOCAL SINGLETON:
 * Manufactures Browser and APIRequestContext instances isolated per thread for safe parallel execution.
 */
public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static final ThreadLocal<APIRequestContext> tlApiContext = new ThreadLocal<>();

    private static void initPlaywright() {
        if (tlPlaywright.get() == null) {
            tlPlaywright.set(Playwright.create());
        }
    }

    // Factory method that creates the requested Browser type
    public static Page initBrowser(String browserName, boolean headless, double slowMo) {
        initPlaywright();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo);

        Browser browser = switch (browserName.toLowerCase().trim()) {
            case "firefox" -> tlPlaywright.get().firefox().launch(options);
            case "webkit"  -> tlPlaywright.get().webkit().launch(options);
            default        -> tlPlaywright.get().chromium().launch(options);
        };

        tlBrowser.set(browser);
        tlContext.set(browser.newContext());
        tlPage.set(tlContext.get().newPage());
        return tlPage.get();
    }

    // Factory method that creates the API client context
    public static APIRequestContext initApi(String baseUrl) {
        initPlaywright();
        APIRequestContext api = tlPlaywright.get().request().newContext(
                new APIRequest.NewContextOptions().setBaseURL(baseUrl)
        );
        tlApiContext.set(api);
        return tlApiContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static APIRequestContext getApiContext() {
        return tlApiContext.get();
    }

    public static void closeAll() {
        if (tlApiContext.get() != null) {
            tlApiContext.get().dispose();
            tlApiContext.remove();
        }
        if (tlPage.get() != null) {
            tlPage.get().close();
            tlPage.remove();
        }
        if (tlContext.get() != null) {
            tlContext.get().close();
            tlContext.remove();
        }
        if (tlBrowser.get() != null) {
            tlBrowser.get().close();
            tlBrowser.remove();
        }
        if (tlPlaywright.get() != null) {
            tlPlaywright.get().close();
            tlPlaywright.remove();
        }
    }
}