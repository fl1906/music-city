package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 地区视图对象 pzc_region
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
public class PzcRegionVo {

    private static final long serialVersionUID = 1L;

    /**
     * 地区id
     */
    @ExcelProperty(value = "地区id")
    private Long regionId;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String base;

    /**
     * 地区名称
     */
    @ExcelProperty(value = "地区名称")
    private String name;

    /**
     * 城市主活动图
     */
    @ExcelProperty(value = "城市主活动图")
    private String imgUrl;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 子地区列表
     */
    private List<PzcRegionVo> children;

    // 构造方法
    public PzcRegionVo() {
        this.children = new ArrayList<>();
    }


}
