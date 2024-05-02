package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 主办方票务视图对象 pzc_organizer_ticket
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Data
@ExcelIgnoreUnannotated
public class PzcOrganizerTicketVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long organizerTicketId;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 二维码图片
     */
    @ExcelProperty(value = "二维码图片")
    private String qrImage;

    /**
     * logo图
     */
    @ExcelProperty(value = "logo图")
    private String logoImage;

    /**
     * 关联主办方ID
     */
    @ExcelProperty(value = "关联主办方ID")
    private Long organizerId;


}
