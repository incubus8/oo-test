import org.hamcrest.core.StringContains;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DataRepoTest {

    private static DataRepo dataRepo = new DataRepo();

    //A valid JSON String to parse.
    String VALID_JSON_STRING = "{ \"developers\": [{ \"firstName\":\"Linus\" , \"lastName\":\"Torvalds\" }, " +
            "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}";

    // Invalid String with a missing parenthesis at the beginning.
    String NOT_VALID_JSON_STRING = "\"developers\": [ \"firstName\":\"Linus\" , \"lastName\":\"Torvalds\" }, " +
            "{ \"firstName\":\"John\" , \"lastName\":\"von Neumann\" } ]}";

    String VALID_XML_STRING = "<note>\n" +
            "<to>Tove</to>\n" +
            "<from>Jani</from>\n" +
            "<heading>Reminder</heading>\n" +
            "<body>Don't forget me this weekend!</body>\n" +
            "</note>";

    @BeforeClass
    public static void setup() {
        dataRepo = new DataRepo();
    }

    @Rule
    public ExpectedException expEx = ExpectedException.none();

    @Test
    public void testRegisterWithEmptyItemName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item name should not empty");

        dataRepo.register(null, VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void testRegisterWithEmptyItemContent() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item content should not empty");

        dataRepo.register("abc", null, DataModel.TYPE_JSON);
    }

    @Test
    public void testRegisterWithInvalidItemType() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Wrong item type");

        dataRepo.register("abc", VALID_JSON_STRING, -1);
    }

    @Test
    public void testRegisterValidJSON() {
        try {
            final String itemName = "Valid Json 1";
            dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_JSON);
            int type = dataRepo.getType(itemName);
            assertEquals("JSON register should return its type", DataModel.TYPE_JSON, type);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRegisterValidXML() {
        try {
            final String itemName = "Valid XML 1";
            dataRepo.register(itemName, VALID_XML_STRING, DataModel.TYPE_XML);
            int type = dataRepo.getType(itemName);
            assertEquals("XML register should return its type", DataModel.TYPE_XML, type);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRegisterInvalidJSON() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage(StringContains.containsString("com.google.gson.stream.MalformedJsonException:"));

        final String itemName = "Invalid XML 1";
        dataRepo.register(itemName, NOT_VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void testRegisterInvalidXML() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage(StringContains.containsString("Content is not allowed in prolog."));

        final String itemName = "Invalid XML 1";
        dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_XML);
    }

    @Test
    public void testRegisterJSONWithSameName() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't register. Item name already exist");

        final String itemName = "Valid Json 1";
        dataRepo.register(itemName, VALID_JSON_STRING, DataModel.TYPE_JSON);
    }

    @Test
    public void testDeregister() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't getType. Item name does not exist");

        final String itemName = "Valid XML 1";
        dataRepo.deregister(itemName);
        dataRepo.getType(itemName);
    }

    @Test
    public void testDeregisterWithNullString() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't deregister. Item name should not empty");

        dataRepo.deregister(null);
    }

    @Test
    public void testDeregisterWithEmptyString() throws Exception {
        expEx.expect(Exception.class);
        expEx.expectMessage("Can't deregister. Item name should not empty");

        dataRepo.deregister("");
    }

}
