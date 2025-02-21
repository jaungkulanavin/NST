package stepdefinitions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightSingleton {

    private static PlaywrightSingleton instance;
    private Playwright playwright;
    private Browser browser;
    private Page page;

    private PlaywrightSingleton() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    public static PlaywrightSingleton getInstance() {
        if (instance == null) {
            instance = new PlaywrightSingleton();
        }
        return instance;
    }

    public Page getPage() {
        return page;
    }

    public void close() {
        if (browser != null) {
            browser.close();
            playwright.close();
            instance = null;
        }
    }
}