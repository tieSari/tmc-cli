package wad;

import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringApplicationConfiguration
public class PuutarhasuunnitteluTest {

    private static final String VEGETABLES_URI = "http://localhost:8080/vegetables";
    private static final String GARDENS_URI = "http://localhost:8080/gardens";

    private ConfigurableApplicationContext context;
    private WebDriver driver;

    @Before
    public void setUp() {
        this.context = SpringApplication.run(Application.class);
        this.driver = new HtmlUnitDriver();
    }

    @After
    public void teardown() {
        SpringApplication.exit(context);
        this.driver.close();
    }

    @Test
    @Points("W5E01.1")
    public void canSeeVegetablesSiteAndMessage() {
        driver.get(VEGETABLES_URI);
        pageHasText("Vegetables!");
    }

    @Test
    @Points("W5E01.2")
    public void canAddVegetable() {
        driver.get(VEGETABLES_URI);

        String veggie = "juhlaporkkana";

        pageDoesNotHaveText(veggie);

        driver.findElement(By.name("name")).sendKeys(veggie);
        driver.findElement(By.name("name")).submit();

        pageHasText(veggie);
    }

    @Test
    @Points("W5E01.2")
    public void seeErrorOnEmptyVegetable() {
        driver.get(VEGETABLES_URI);

        String veggie = "";

        pageDoesNotHaveText("thou shalt not");

        driver.findElement(By.name("name")).sendKeys(veggie);
        driver.findElement(By.name("name")).submit();

        pageHasText("thou shalt not");
    }

    @Test
    @Points("W5E01.2")
    public void canAddRandomVegetable() {
        driver.get(VEGETABLES_URI);

        String veggie = UUID.randomUUID().toString().substring(0, 10);

        pageDoesNotHaveText(veggie);

        driver.findElement(By.name("name")).sendKeys(veggie);
        driver.findElement(By.name("name")).submit();

        pageHasText(veggie);
    }

    @Test
    @Points("W5E01.2")
    public void canAddMultipleRandomVegetablesInARow() {
        driver.get(VEGETABLES_URI);

        List<String> veggies = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String veggie = UUID.randomUUID().toString().substring(0, 10);
            veggies.add(veggie);

            pageDoesNotHaveText(veggie);

            driver.findElement(By.name("name")).sendKeys(veggie);
            driver.findElement(By.name("name")).submit();

        }

        for (String veggy : veggies) {
            pageHasText(veggy);
        }
    }

    @Test
    @Points("W5E01.3")
    public void canSeeGardenSite() {
        driver.get(GARDENS_URI);
        pageHasText("gardens");
    }

    @Test
    @Points("W5E01.3")
    public void canAddGarden() {
        driver.get(GARDENS_URI);

        String garden = "mika naurismaa";

        pageDoesNotHaveText(garden);

        driver.findElement(By.name("name")).sendKeys(garden);
        driver.findElement(By.name("name")).submit();

        pageHasText(garden);
    }

    @Test
    @Points("W5E01.3")
    public void seeErrorOnEmptyGarden() {
        driver.get(GARDENS_URI);

        String garden = "";

        pageDoesNotHaveText("thou shalt not");

        driver.findElement(By.name("name")).sendKeys(garden);
        driver.findElement(By.name("name")).submit();

        pageHasText("thou shalt not");
    }

    @Test
    @Points("W5E01.3")
    public void canAddMultipleRandomGardensInARow() {
        driver.get(GARDENS_URI);

        List<String> gardens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String garden = UUID.randomUUID().toString().substring(0, 10);
            gardens.add(garden);

            pageDoesNotHaveText(garden);

            driver.findElement(By.name("name")).sendKeys(garden);
            driver.findElement(By.name("name")).submit();

        }

        for (String garden : gardens) {
            pageHasText(garden);
        }
    }

    @Test
    @Points("W5E01.4")
    public void canSeePlantAVegetableText() {
        driver.get(VEGETABLES_URI);

        // there will be a vegetable that can be planted after this
        String veggie = UUID.randomUUID().toString().substring(0, 10);
        pageDoesNotHaveText(veggie);
        driver.findElement(By.name("name")).sendKeys(veggie);
        driver.findElement(By.name("name")).submit();

        driver.get(GARDENS_URI);
        String garden = UUID.randomUUID().toString().substring(0, 10);
        pageDoesNotHaveText(garden);

        driver.findElement(By.name("name")).sendKeys(garden);
        driver.findElement(By.name("name")).submit();

        // there should be at least one garden where we can plant vegetables
        // (in reality, much more as other tests have been also executed)
        pageHasText("plant a vegetable");
    }

    @Test
    @Points("W5E01.4")
    public void canPlantAVegetable() {
        driver.get(VEGETABLES_URI);

        // there will be a vegetable that can be planted after this
        String veggie = UUID.randomUUID().toString().substring(0, 10);
        pageDoesNotHaveText(veggie);
        driver.findElement(By.name("name")).sendKeys(veggie);
        driver.findElement(By.name("name")).submit();

        driver.get(GARDENS_URI);
        driver.navigate();
        
        String garden = UUID.randomUUID().toString().substring(0, 10);
        pageDoesNotHaveText(garden);

        driver.findElement(By.name("name")).sendKeys(garden);
        driver.findElement(By.name("name")).submit();

        // there should be at least one garden where we can plant vegetables
        // (in reality, much more as other tests have been also executed)
        pageHasText("plant a vegetable");
        

        new Select(driver.findElement(By.name("vegetableId"))).selectByVisibleText(veggie);
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();

        
        // vegetable should be added; let's see that it's in the correct area
        // note that this tests the success of only a single addition
        assertEquals(veggie, driver.findElement(By.xpath("//ol/li")).getText());
    }

    private void pageHasText(String text) {
        assertTrue(driver.getPageSource().toLowerCase().contains(text.toLowerCase()));
    }

    private void pageDoesNotHaveText(String text) {
        assertFalse(driver.getPageSource().toLowerCase().contains(text.toLowerCase()));
    }
}
