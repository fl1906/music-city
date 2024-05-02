package top.flya.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import top.flya.common.core.domain.R;
import top.flya.system.utils.gaode.GaoDeMapUtil;

import javax.annotation.Resource;


/**
 * @Description: 地图控制层
 * @Author: isymikasan
 * @Date: 2022-01-26 09:36:55
 */
@RestController

@RequestMapping("/point")
public class GaoDeMapController {

    @Resource
    private GaoDeMapUtil gaoDeMapUtil;

    public static final Logger log = LoggerFactory.getLogger(GaoDeMapController.class);


    @GetMapping("/getAddress")
    public R getAddress(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude) {
        try {
            return gaoDeMapUtil.getAddress(longitude, latitude);
        } catch (Exception e) {
            return R.fail(e.toString());
        }

    }

    @GetMapping("/getLonLat")
    public R getLonLat(@RequestParam("address") String address) {
        return gaoDeMapUtil.getLonLat(address);
    }

    @GetMapping("/getDistance")
    public R getDistance(@RequestParam("start") String startLonLat,@RequestParam("end") String endLonLat) {
        return gaoDeMapUtil.getDistance(startLonLat, endLonLat);
    }
}
