import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;
import org.junit.runners.Suite;
import tariffManagementService.TariffTests;

@Suite.SuiteClasses({TariffTests.class})
@Categories.IncludeCategory(AllTests.class)
@RunWith(Categories.class)

public class RunTestsSuite {
}
