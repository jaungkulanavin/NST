package Runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)

/*@CucumberOptions(
		plugin = {"json:target/build/test-report/cucumber.json" , "html:target/build/test-report/reports.html"},
        features = "src/test/resources/features",
        glue = "stepdefinitions",
        dryRun = false,
        tags = "@UC_NGS_ASM01"
		)

 */

/*@CucumberOptions(
		plugin = {"json:target/build/test-report/cucumber.json" , "html:target/build/test-report/reports.html"},
		features = "src/test/resources/features",
		glue = "stepdefinitions",
		dryRun = false,
		tags = "@UC_NGS_ASM02"
)*/

@CucumberOptions(
		plugin = {"json:target/build/test-report/cucumber.json" , "html:target/build/test-report/reports.html"},
		features = "src/test/resources/features",
		glue = "stepdefinitions",
		dryRun = false,
		tags = "@UC_NGS_ASM03"
)



public class Runner {}

