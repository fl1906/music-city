package top.flya.system.utils.map;

public class City {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.endsWith("市"))
        {
            name = name.substring(0, name.length() - 1);
        }
        this.name = name;
    }
}
