package top.flya.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import top.flya.common.annotation.ExcelDictFormat;
import top.flya.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 艺人视图对象 pzc_artist
 *
 * @author flya
 * @date 2023-06-01
 */
@Data
@ExcelIgnoreUnannotated
@AllArgsConstructor
@NoArgsConstructor
public class PzcArtistVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private Long artistId;

    /**
     * 艺人名
     */
    @ExcelProperty(value = "艺人名")
    private String name;

    /**
     * 艺人照片
     */
    @ExcelProperty(value = "艺人照片")
    private String imageUrl;

    /**
     * 艺人介绍
     */
    @ExcelProperty(value = "艺人介绍")
    private String description;


}
