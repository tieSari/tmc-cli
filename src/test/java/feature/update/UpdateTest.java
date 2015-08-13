package feature.update;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class) @CucumberOptions(features = {"src/test/resources/features/update.feature"})

public class UpdateTest {
}
