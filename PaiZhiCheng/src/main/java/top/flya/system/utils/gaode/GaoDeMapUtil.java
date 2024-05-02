package top.flya.system.utils.gaode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flya.common.core.domain.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Description: 高德地图工具类
 * @Author: isymikasan
 * @Date: 2021-12-22 09:19:02
 */
@Component
@Slf4j

public class GaoDeMapUtil {

    /**
     * 功能描述: 高德地图Key
     *
     * @param null
     * @return
     * @author 周兆宇
     * @date 2022-01-26 09:13:40
     */
    private static final String GAO_DE_KEY = "112049d76fe83e408d4ecceafb2ad4e3";

    //申请的账户Key

    /**
     * 功能描述: 根据地址名称得到两个地址间的距离
     *
     * @param start 起始位置
     * @param end   结束位置
     * @return long 两个地址间的距离
     * @author isymikasan
     * @date 2022-01-26 09:16:04
     */
    public Long getDistanceByAddress(String start, String end) {
        String startLonLat = getLonLat(start).getData().toString();
        String endLonLat = getLonLat(end).getData().toString();
        Long distance = Long.valueOf(getDistance(startLonLat, endLonLat).getData().toString());
        return distance;
    }

    /**
     * 功能描述: 地址转换为经纬度
     *
     * @param address 地址
     * @return java.lang.String 经纬度
     * @author isymikasan
     * @date 2022-01-26 09:17:13
     */
    public R getLonLat(String address) {
            log.info("地址为：" + address);
            // 返回输入地址address的经纬度信息, 格式是 经度,纬度
            String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key=" + GAO_DE_KEY + "&address=" + address;
            // 高德接口返回的是JSON格式的字符串
            String queryResult = getResponse(queryUrl);
            JSONObject job = JSONObject.parseObject(queryResult);
            log.info("高德接口返回的是JSON格式的字符串：" + queryResult);
            JSONObject jobJSON = JSONObject
                .parseObject(
                    job.get("geocodes").toString().substring(1, job.get("geocodes").toString().length() - 1));
            String LngAndLat = jobJSON.get("location").toString();
            log.info("经纬度为：" + LngAndLat);
            return R.ok("经纬度转换成功！",LngAndLat);
    }

    /**
     * 将经纬度 转换为 地址
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 地址名称
     * @throws Exception
     */
    public static R getAddress(String longitude, String latitude) throws Exception {
        String url;
        try {
            url = "http://restapi.amap.com/v3/geocode/regeo?output=JSON&location=" + longitude + "," + latitude
                + "&key=" + GAO_DE_KEY + "&radius=0&extensions=base";

            log.info("经度 " + longitude);
            log.info("纬度：" + latitude);
            log.info("url:" + url);

            // 高德接口返回的是JSON格式的字符串
            String queryResult = getResponse(url);
            if (ObjectUtils.isNull(queryResult)) {
                return R.fail("查询结果为空");
            }

            // 将获取结果转为json 数据
            JSONObject obj = JSONObject.parseObject(queryResult);
            if (obj.get(GaoDeEnum.STATUS.getCode()).toString().equals(GaoDeEnum.INT_ONE.getCode())) {
                // 如果没有返回-1
                JSONObject reGeoCode = obj.getJSONObject(GaoDeEnum.RE_GEO_CODE.getCode());
                if (reGeoCode.size() > 0) {
                    log.info("reGeoCode:" + reGeoCode);
                    // 在regeocode中拿到 formatted_address 具体位置
                    String formatted = reGeoCode.get("formatted_address").toString();
                    return R.ok( "地址获取成功！",formatted);

                } else {
                    return R.fail("未找到相匹配的地址！");
                }
            } else {
                return R.fail("请求错误！");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return R.fail("系统未知异常，请稍后再试");
        }
    }

    /**
     * 功能描述: 根据两个定位点的经纬度算出两点间的距离
     *
     * @param startLonLat 起始经纬度
     * @param endLonLat   结束经纬度（目标经纬度）
     * @return java.lang.Long 两个定位点之间的距离
     * @author isymikasan
     * @date 2022-01-26 09:47:42
     */
    public R<Long> getDistance(String startLonLat, String endLonLat) {
        try {
            // 返回起始地startAddr与目的地endAddr之间的距离，单位：米
            Long result = new Long(0);
            String queryUrl =
                "http://restapi.amap.com/v3/distance?key=" + GAO_DE_KEY + "&origins=" + startLonLat
                    + "&destination="
                    + endLonLat;
            String queryResult = getResponse(queryUrl);
            log.info("请求url is {} \n高德接口返回的是JSON格式的字符串：{}" ,queryUrl,queryResult);
            JSONObject job = JSONObject.parseObject(queryResult);
            JSONArray ja = job.getJSONArray("results");
            if(ja.size() == 0){
                return R.ok(0L); //距离计算失败
            }
            JSONObject jobO = JSONObject.parseObject(ja.getString(0));
            result = Long.parseLong(jobO.get("distance").toString());
            return R.ok("距离计算成功！",result);
        } catch (Exception e) {
            return R.fail(e.toString());
        }


    }

    /**
     * 功能描述: 发送请求
     *
     * @param serverUrl 请求地址
     * @return java.lang.String
     * @author isymikasan
     * @date 2022-01-26 09:15:01
     */
    private static String getResponse(String serverUrl) {
        // 用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getAddress( "118.73145","32.00335"));
    }

}
