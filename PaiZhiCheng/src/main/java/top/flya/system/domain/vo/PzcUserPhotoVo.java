package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 用户资料相册视图对象 pzc_user_photo
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Data
@ExcelIgnoreUnannotated
public class PzcUserPhotoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 照片ID
     */
    @ExcelProperty(value = "照片ID")
    private Long photoId;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 照片
     */
    @ExcelProperty(value = "照片")
    private String url;

    /**
     * 照片说明
     */
    @ExcelProperty(value = "照片说明")
    private String message;

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


}
