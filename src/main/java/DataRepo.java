import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class DataRepo {

    private Map<String, DataModel> data;

    public DataRepo() {
        data = new HashMap();
    }

    public void register(String itemName, String itemContent, int itemType) throws Exception {
        if (StringUtils.isEmpty(itemName)) {
            throw new Exception("Can't register. Item name should not empty");
        }

        if (StringUtils.isEmpty(itemContent)) {
            throw new Exception("Can't register. Item content should not empty");
        }

        switch (itemType) {
            case DataModel.TYPE_JSON:
                loadJson(itemContent);
                break;
            case DataModel.TYPE_XML:
                loadXML(itemContent);
                break;
            default:
                throw new Exception("Can't register. Wrong item type");
        }

        if (this.data.containsKey(itemName)) {
            throw new Exception("Can't register. Item name already exist");
        }

        this.data.put(itemName, new DataModel(itemName, itemContent, itemType));
    }

    public void deregister(String itemName) throws Exception {
        if (StringUtils.isEmpty(itemName)) {
            throw new Exception("Can't deregister. Item name should not empty");
        }

        if (this.data.containsKey(itemName)) {
            throw new Exception("Can't deregister. Item name does not exist");
        }

        this.data.remove(itemName);
    }

    public int getType(String itemName) throws Exception {
        if (StringUtils.isEmpty(itemName)) {
            throw new Exception("Can't getType. Item name should not empty");
        }

        if (!this.data.containsKey(itemName)) {
            throw new Exception("Can't getType. Item name does not exist");
        }

        return this.data.get(itemName).getType();
    }

    private void loadJson(String itemContent) throws Exception {
        new Gson().fromJson(itemContent, Object.class);
    }

    private void loadXML(String itemContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(itemContent));
        builder.parse(is);
    }

}
