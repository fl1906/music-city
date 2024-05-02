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
import top.flya.system.domain.vo.PzcOrganizerVo;
import top.flya.system.domain.bo.PzcOrganizerBo;
import top.flya.system.service.IPzcOrganizerService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 活动主办方
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/organizer")
public class PzcOrganizerController extends BaseController {

    private final IPzcOrganizerService iPzcOrganizerService;

    /**
     * 查询活动主办方列表
     */
    @SaCheckPermission("system:organizer:list")
    @GetMapping("/list")
    public TableDataInfo<PzcOrganizerVo> list(PzcOrganizerBo bo, PageQuery pageQuery) {
        return iPzcOrganizerService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动主办方列表
     */
    @SaCheckPermission("system:organizer:export")
    @Log(title = "活动主办方", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcOrganizerBo bo, HttpServletResponse response) {
        List<PzcOrganizerVo> list = iPzcOrganizerService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动主办方", PzcOrganizerVo.class, response);
    }

    /**
     * 获取活动主办方详细信息
     *
     * @param organizerId 主键
     */
    @SaCheckPermission("system:organizer:query")
    @GetMapping("/{organizerId}")
    public R<PzcOrganizerVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long organizerId) {
        return R.ok(iPzcOrganizerService.queryById(organizerId));
    }

    /**
     * 新增活动主办方
     */
    @SaCheckPermission("system:organizer:add")
    @Log(title = "活动主办方", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcOrganizerBo bo) {
        return toAjax(iPzcOrganizerService.insertByBo(bo));
    }

    /**
     * 修改活动主办方
     */
    @SaCheckPermission("system:organizer:edit")
    @Log(title = "活动主办方", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcOrganizerBo bo) {
        return toAjax(iPzcOrganizerService.updateByBo(bo));
    }

    /**
     * 删除活动主办方
     *
     * @param organizerIds 主键串
     */
    @SaCheckPermission("system:organizer:remove")
    @Log(title = "活动主办方", businessType = BusinessType.DELETE)
    @DeleteMapping("/{organizerIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] organizerIds) {
        return toAjax(iPzcOrganizerService.deleteWithValidByIds(Arrays.asList(organizerIds), true));
    }
}
