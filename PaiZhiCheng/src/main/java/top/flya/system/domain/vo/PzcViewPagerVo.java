package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 轮播图视图对象 pzc_view_pager
 *
 * @author ruoyi
 * @date 2023-05-23
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
public class PzcViewPagerVo {

    private static final long serialVersionUID = 1L;

    /**
     * 轮播图id
     */
    @ExcelProperty(value = "轮播图id")
    private Integer viewPagerId;

    /**
     * 轮播图名称
     */
    @ExcelProperty(value = "轮播图名称")
    private String name;

    /**
     * 轮播图图片Url
     */
    @ExcelProperty(value = "轮播图图片Url")
    private String imageUrl;

    /**
     * 轮播图链接Url
     */
    @ExcelProperty(value = "轮播图链接Url")
    private String linkUrl;

    /**
     * 删除状态，默认为1表示正常状态
     */
    @ExcelProperty(value = "删除状态，默认为1表示正常状态")
    private Integer state;

    /**
     * 关联活动id  0表示不关联
     */
    @ExcelProperty(value = "关联活动id  0表示不关联")
    private Long activityId;


}
