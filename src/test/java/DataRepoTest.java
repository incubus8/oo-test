import org.hamcrest.core.StringContains;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataRepoTest {

    private static DataRepo dataRepo = new DataRepo();

    //A valid JSON String to parse.
    private final String VALID_JSON_STRING = "{ \"developers\": [{ \"firstName\":\"Linus\" , \"lastName\":\"Torvalds\" }, " +
            "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}";

    // Invalid String with a missing parenthesis at the beginning.
    private final String NOT_VALID_JSON_STRING = "\"developers\": [ \"firstName\":\"Linus\" , \"lastName\":\"Torvalds\" }, " +
            "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}";

    private final String VALID_XML_STRING = "<note>\n" +
            "<to>Tove</to>\n" +
            "<from>Jani</from>\n" +
            "<heading>Reminder</heading>\n" +
            "<body>Don't forget me this weekend!</body>\n" +
            "</note>";

    @BeforeClass
    public static void setup() {
        dataRepo = new DataRepo();
    }

    @AfterClass
    public static void tearDown() {
        dataRepo.clear();
    }

    @Rule
    public ExpectedException expEx = ExpectedException.none();

    @Test
    public void test1_RegisterWithEmptyItemName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item name should not empty");

        dataRepo.register(null, VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void test2_RegisterWithEmptyItemContent() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item content should not empty");

        dataRepo.register("abc", null, DataModel.TYPE_JSON);
    }

    @Test
    public void test3_RegisterWithInvalidItemType() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Wrong item type");

        dataRepo.register("abc", VALID_JSON_STRING, -1);
    }

    @Test
    public void test4_RegisterValidJSON() {
        try {
            final String itemName = "Valid Json";
            dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_JSON);
            int type = dataRepo.getType(itemName);
            assertEquals("JSON register should return its type", DataModel.TYPE_JSON, type);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test5_RegisterJSONWithSameName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item name already exist");

        final String itemName = "Valid Json";
        dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void test6_RegisterValidXML() {
        try {
            final String itemName = "Valid XML";
            dataRepo.register(itemName, VALID_XML_STRING, DataModel.TYPE_XML);
            int type = dataRepo.getType(itemName);
            assertEquals("XML register should return its type", DataModel.TYPE_XML, type);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void test7_RegisterInvalidJSON() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage(StringContains.containsString("com.google.gson.stream.MalformedJsonException:"));

        final String itemName = "Invalid JSON";
        dataRepo.register(itemName, NOT_VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void test8_RegisterInvalidXML() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage(StringContains.containsString("Content is not allowed in prolog."));

        final String itemName = "Invalid XML";
        dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_XML);
    }

    @Test
    public void test9_Deregister() throws Exception {
        final String itemName = "Valid XML";
        dataRepo.deregister(itemName);
    }

    @Test
    public void test10_DeregisterForTheSecondTime() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't deregister. Item name does not exist");

        final String itemName = "Valid XML";
        dataRepo.deregister(itemName);
    }

    @Test
    public void test11_DeregisterWithNullString() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't deregister. Item name should not empty");

        dataRepo.deregister(null);
    }

    @Test
    public void test12_DeregisterWithEmptyString() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't deregister. Item name should not empty");

        dataRepo.deregister("  ");
    }

    @Test
    public void test13_GetTypeWithEmptyName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't getType. Item name should not empty");
        dataRepo.getType("  ");
    }

    @Test
    public void test14_GetTypeWithNullName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't getType. Item name should not empty");

        dataRepo.getType(null);
    }

    @Test
    public void test15_GetTypeWithInvalidKey() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't getType. Item name does not exist");

        final String itemName = "Valid XML";
        dataRepo.getType(itemName);
    }

}
