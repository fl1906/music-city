package top.flya.system.controller;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import top.flya.common.annotation.RepeatSubmit;
import top.flya.common.annotation.Log;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.domain.R;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
import top.flya.common.enums.BusinessType;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.vo.PzcActivityConnArtistVo;
import top.flya.system.domain.bo.PzcActivityConnArtistBo;
import top.flya.system.service.IPzcActivityConnArtistService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 活动关联艺人
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/activityConnArtist")
public class PzcActivityConnArtistController extends BaseController {

    private final IPzcActivityConnArtistService iPzcActivityConnArtistService;

    /**
     * 查询活动关联艺人列表
     */
    @SaCheckPermission("system:activityConnArtist:list")
    @GetMapping("/list")
    public TableDataInfo<PzcActivityConnArtistVo> list(PzcActivityConnArtistBo bo, PageQuery pageQuery) {
        return iPzcActivityConnArtistService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动关联艺人列表
     */
    @SaCheckPermission("system:activityConnArtist:export")
    @Log(title = "活动关联艺人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityConnArtistBo bo, HttpServletResponse response) {
        List<PzcActivityConnArtistVo> list = iPzcActivityConnArtistService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动关联艺人", PzcActivityConnArtistVo.class, response);
    }

    /**
     * 获取活动关联艺人详细信息
     *
     * @param activityConnArtistId 主键
     */
    @SaCheckPermission("system:activityConnArtist:query")
    @GetMapping("/{activityConnArtistId}")
    public R<PzcActivityConnArtistVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer activityConnArtistId) {
        return R.ok(iPzcActivityConnArtistService.queryById(activityConnArtistId));
    }

    /**
     * 新增活动关联艺人
     */
    @SaCheckPermission("system:activityConnArtist:add")
    @Log(title = "活动关联艺人", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityConnArtistBo bo) {
        return toAjax(iPzcActivityConnArtistService.insertByBo(bo));
    }

    /**
     * 修改活动关联艺人
     */
    @SaCheckPermission("system:activityConnArtist:edit")
    @Log(title = "活动关联艺人", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityConnArtistBo bo) {
        return toAjax(iPzcActivityConnArtistService.updateByBo(bo));
    }

    /**
     * 删除活动关联艺人
     *
     * @param activityConnArtistIds 主键串
     */
    @SaCheckPermission("system:activityConnArtist:remove")
    @Log(title = "活动关联艺人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityConnArtistIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] activityConnArtistIds) {
        return toAjax(iPzcActivityConnArtistService.deleteWithValidByIds(Arrays.asList(activityConnArtistIds), true));
    }
}
