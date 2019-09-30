package tariffManagementService;

import commonTestClasses.FailedResponse;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runners.AllTests;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;
import static tariffManagementService.Tariff.*;

public class TariffTests {
    private static int tariffsNumber;
    private static ArrayList<Integer> tariffsListToBeSaved = new ArrayList<>();
    private final static String TARIFF_NAME_TO_DELETE = "NewTestTariffToDelete";
    private final static String VALID_TARIFF_NAME = "NewTestTariff";
    private final static String INVALID_TARIFF_NAME = "TariffWillNeverExist";
    private final static String TARIFF_NAME_TO_DUPLICATE = "DuplicatedTariffName";
    private final static float VALID_TARIFF_PRICE = 4.67f;
    private final static int VALID_TARIFF_WASHING_TIME = 12;
    private final static float INVALID_TARIFF_PRICE = -1.24f;
    private final static int INVALID_TARIFF_WASHING_TIME = -7;
    private final static int DELTA = 0;
    private final static String INVALID_PARAMETER_MESSAGE = "Tariff not created, price and washing time should be a non negative";
    private final static String NON_UNIQUE_NAME_MESSAGE = "Tariff not created, tariff with name " + TARIFF_NAME_TO_DUPLICATE + " already exists";
    private final static String NO_TARIFF_MESSAGE = "No tariff with id %s found";

    @BeforeClass
    public static void configureRestAssured() {
        port = 8092;
        for (TariffObject tariff : getTariffsList())
            tariffsListToBeSaved.add(tariff.getId());
    }

    @Before
    public void setCommonVariables() {
        tariffsNumber = getTariffsList().length;
    }

    /**
     * 1. Create new tariff using POST method.
     * 2. Verify that number of tariffs has been increased by 1.
     * 3. Get new tariff by id using GET method.
     * 4. Verify that new tariff is the same as expected.
     */
    @Test
    @Category({TariffTests.class, AllTests.class})
    public void testNewTariffSuccessfulCreation() {
        TariffObject newTestTariff = createTariffByPost(VALID_TARIFF_NAME, VALID_TARIFF_PRICE, VALID_TARIFF_WASHING_TIME);
        assertEquals(tariffsNumber + 1, getTariffsList().length);
        TariffObject newTestTariffById = getTariffById(newTestTariff.getId());

        assertEquals(newTestTariff.getId(), newTestTariffById.getId());
        assertEquals(newTestTariff.getName(), newTestTariffById.getName());
        assertEquals(newTestTariff.getPrice(), newTestTariffById.getPrice(), DELTA);
        assertEquals(newTestTariff.getWashingTime(), newTestTariffById.getWashingTime());
    }

    /**
     * 1. Create new tariff using POST method.
     * 2. Delete created tariff by id using DELETE method.
     * 3. Try to get deleted tariff by id using GET method (500 response).
     * 4. Verify that tariffs list has the same size as before creating.
     */
    @Test
    @Category({TariffTests.class, AllTests.class})
    public void testNewTariffSuccessfulDeletion() {
        TariffObject newTestTariff = createTariffByPost(TARIFF_NAME_TO_DELETE, VALID_TARIFF_PRICE, VALID_TARIFF_WASHING_TIME);
        deleteTariffById(newTestTariff.getId());
        FailedResponse failedResponse = getTariffByIdWithError(newTestTariff.getId());
        assertEquals(String.format(NO_TARIFF_MESSAGE, newTestTariff.getId()), failedResponse.getMessage());
        assertEquals(tariffsNumber, getTariffsList().length);
    }

    /**
     * 1. Try to create new tariff with invalid price using POST method.
     * 2. Verify that tariff hasn't been created (response message, tariffs list).
     */
    @Test
    @Category({TariffTests.class, AllTests.class})
    public void testNewTariffCreationWithInvalidPrice() {
        FailedResponse failedResponse = createTariffByPostWithError(INVALID_TARIFF_NAME, INVALID_TARIFF_PRICE, VALID_TARIFF_WASHING_TIME);
        assertEquals(INVALID_PARAMETER_MESSAGE, failedResponse.getMessage());
        assertEquals(tariffsNumber, getTariffsList().length);
    }

    /**
     * 1. Try to create new tariff with invalid washingTime using POST method.
     * 2. Verify that tariff hasn't been created (response message, tariffs list).
     */
    @Test
    @Category({TariffTests.class, AllTests.class})
    public void testNewTariffCreationWithInvalidWashingTime() {
        FailedResponse failedResponse = createTariffByPostWithError(INVALID_TARIFF_NAME, VALID_TARIFF_PRICE, INVALID_TARIFF_WASHING_TIME);
        assertEquals(INVALID_PARAMETER_MESSAGE, failedResponse.getMessage());
        assertEquals(tariffsNumber, getTariffsList().length);
    }

    /**
     * 1. Create new tariff using POST method.
     * 2. Try to create one more tariff with the same name using POST method.
     * 3. Verify that second tariff hasn't been created (response message, tariffs list).
     */
    @Test
    @Category({TariffTests.class, AllTests.class})
    public void testNewTariffCreationWithNonUniqueName() {
        createTariffByPost(TARIFF_NAME_TO_DUPLICATE, VALID_TARIFF_PRICE, VALID_TARIFF_WASHING_TIME);
        FailedResponse failedResponse = createTariffByPostWithError(TARIFF_NAME_TO_DUPLICATE, VALID_TARIFF_PRICE, VALID_TARIFF_WASHING_TIME);
        assertEquals(NON_UNIQUE_NAME_MESSAGE, failedResponse.getMessage());
        assertEquals(tariffsNumber + 1, getTariffsList().length);
    }

    @AfterClass
    public static void resetTestData() {
        for (TariffObject tariff : getTariffsList())
            if (!tariffsListToBeSaved.contains(tariff.getId()))
                deleteTariffById(tariff.getId());
    }
}
