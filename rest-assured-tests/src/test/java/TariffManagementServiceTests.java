import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class TariffManagementServiceTests {
    private static int tariffsNumber;
    private final static String TARIFF_NAME = "NewTestTariff";
    private final static float TARIFF_PRICE = 4.67f;
    private final static int TARIFF_WASHING_TIME = 12;
    private final static int DELTA = 0;

    @BeforeClass
    public static void configureRestAssured() {
        port = 8092;
        tariffsNumber = TariffManagementServiceClass.getTariffsList().length;
    }

    /**
     * 1. Create new tariff using POST method.
     * 2. Verify that number of tariffs has been increased by 1.
     * 3. Get new tariff by id using GET method.
     * 4. Verify that new tariff is the same as expected.
     */
    @Test
    public void testNewTariffSuccessfulCreation() {
        Tariff newTestTariff = TariffManagementServiceClass.createTariffByPost(TARIFF_NAME, TARIFF_PRICE, TARIFF_WASHING_TIME);
        assertEquals(tariffsNumber + 1, TariffManagementServiceClass.getTariffsList().length);
        Tariff newTestTariffById = TariffManagementServiceClass.getTariffById(newTestTariff.id);

        assertEquals(newTestTariff.id, newTestTariffById.id);
        assertEquals(newTestTariff.name, newTestTariffById.name);
        assertEquals(newTestTariff.price, newTestTariffById.price, DELTA);
        assertEquals(newTestTariff.washingTime, newTestTariffById.washingTime);
    }
}
