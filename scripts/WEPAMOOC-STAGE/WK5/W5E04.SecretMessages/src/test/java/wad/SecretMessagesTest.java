package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringApplicationConfiguration
@Points("W5E04")
public class SecretMessagesTest {

    private static final String MESSAGES_URI = "http://localhost:8080/messages";

    private ConfigurableApplicationContext context;
    private WebDriver driver;

    @Before
    public void setUp() {
        this.context = SpringApplication.run(Application.class);
        this.driver = new HtmlUnitDriver();
    }

    @After
    public void teardown() {
        this.driver.close();
        SpringApplication.exit(context);
    }

    @Test
    public void pageShouldNotBeDirectlyAccessible() {
        this.driver.get(MESSAGES_URI);
        assertFalse(hasText("Jeff Davis"));
    }

    @Test
    public void shouldSeeLoginPageOnAccessingMessages() {
        this.driver.get(MESSAGES_URI);
        assertNotNull(driver.findElement(By.name("username")));
        assertNotNull(driver.findElement(By.name("password")));
    }

    @Test
    public void noAuthOnWrongPassword() {
        this.driver.get(MESSAGES_URI);
        enterDetailsAndSubmit("Onni", "v123");
        assertFalse(hasText("Jeff Davis"));
    }

    @Test
    public void authSuccessfulOnCorrectPassword() {
        this.driver.get(MESSAGES_URI);
        enterDetailsAndSubmit("maxwell_smart", "kenkapuhelin");
        assertTrue(hasText("Jeff Davis"));
    }

    private void enterDetailsAndSubmit(String username, String password) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password")).submit();
    }

    private boolean hasText(String text) {
        return driver.getPageSource().toLowerCase().contains(text.toLowerCase());
    }
}
