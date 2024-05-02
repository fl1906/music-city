package top.flya.system.utils.gaode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 高德地图枚举类
 * @Author: isymikasan
 * @Date: 2022-01-26 09:36:55
 */
@AllArgsConstructor
@Getter
public enum GaoDeEnum {

    // 高德地图固定字段
    STATUS("status"),
    INT_ONE("1"),
    RE_GEO_CODE("regeocode"),
    GEO_CODES("geocodes"),
    LOCATION("location"),
    FORMATTED_ADDRESS("formatted_address"),
    RESULTS("results"),
    DISTANCE("distance");

    private String code;
}
