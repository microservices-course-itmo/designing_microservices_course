import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class TariffManagementServiceTests {
    private static int tariffsNumber;

    @BeforeClass
    public static void configureRestAssured() {
        port = 8092;
        tariffsNumber = TariffManagementServiceClass.getTariffsList().length;
    }

    @Test
    public void testNewTariffSuccessfulCreation() {
        Tariff newTestTariff = TariffManagementServiceClass.createTariffByPost("NewTestTariff", 4.67f, 12);

        assertEquals(tariffsNumber + 1, TariffManagementServiceClass.getTariffsList().length);
        Tariff newTestTariffById = TariffManagementServiceClass.getTariffById(newTestTariff.id);

        assertEquals(newTestTariff.id, newTestTariffById.id);
        assertEquals(newTestTariff.name, newTestTariffById.name);
        assertEquals(newTestTariff.price, newTestTariffById.price, 0);
        assertEquals(newTestTariff.washingTime, newTestTariffById.washingTime);
    }
}
