package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 活动关联艺人视图对象 pzc_activity_conn_artist
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Data
@ExcelIgnoreUnannotated
public class PzcActivityConnArtistVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Integer activityConnArtistId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Integer activityId;

    /**
     * 艺人ID
     */
    @ExcelProperty(value = "艺人ID")
    private Integer artistId;

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
     * 删除状态，默认为1表示正常状态
     */
    @ExcelProperty(value = "删除状态，默认为1表示正常状态")
    private Integer state;


}
