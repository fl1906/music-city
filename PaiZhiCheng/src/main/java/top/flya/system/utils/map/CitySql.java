package top.flya.system.utils.map;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Data
public class CitySql {

    public static void main(String[] args) throws IOException {

        try {
            File file = new File("J:\\ReStudyVue\\paizhi-city\\PaiZhiCheng\\src\\main\\java\\top\\flya\\system\\utils\\map\\city.json");
            String content = FileUtils.readFileToString(file, "UTF-8");
            // Do something with the data
            System.out.println("Hello World!");
            List<Maps> maps = JSONObject.parseArray(content, Maps.class);
//            List<Maps> maps = JsonUtils.parseArray(content, Maps.class);
            String sql = "INSERT INTO paizhicheng.pzc_region (base, name, img_url, create_time, update_time, state)\n" +
                "VALUES ('江苏省', '扬州', DEFAULT, DEFAULT, DEFAULT, DEFAULT);";

            StringBuilder sb = new StringBuilder();
            for (Maps map : maps) {
                String base = map.getName();
                List<City> city = map.getCity();
                for (City maps1 : city) {
                    String name = maps1.getName();
                    String sql1 = sql.replace("江苏省", base).replace("扬州", name);
                    System.out.println(sql1);
                    sb.append(sql1).append("\n");
                }
            }
            System.out.println(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
