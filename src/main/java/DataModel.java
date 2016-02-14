public class DataModel {
    private String name;
    private String content;
    private int type;

    public static final int TYPE_JSON = 1;
    public static final int TYPE_XML = 2;

    public DataModel(String name, String content, int type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {

        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public int getType() {
        return this.type;
    }

}
