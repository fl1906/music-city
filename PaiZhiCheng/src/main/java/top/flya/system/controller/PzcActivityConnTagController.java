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
import top.flya.system.domain.vo.PzcActivityConnTagVo;
import top.flya.system.domain.bo.PzcActivityConnTagBo;
import top.flya.system.service.IPzcActivityConnTagService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 活动标签与活动关联
 *
 * @author ruoyi
 * @date 2023-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/activityConnTag")
public class PzcActivityConnTagController extends BaseController {

    private final IPzcActivityConnTagService iPzcActivityConnTagService;

    /**
     * 查询活动标签与活动关联列表
     */
    @SaCheckPermission("system:activityConnTag:list")
    @GetMapping("/list")
    public TableDataInfo<PzcActivityConnTagVo> list(PzcActivityConnTagBo bo, PageQuery pageQuery) {
        return iPzcActivityConnTagService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动标签与活动关联列表
     */
    @SaCheckPermission("system:activityConnTag:export")
    @Log(title = "活动标签与活动关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityConnTagBo bo, HttpServletResponse response) {
        List<PzcActivityConnTagVo> list = iPzcActivityConnTagService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动标签与活动关联", PzcActivityConnTagVo.class, response);
    }

    /**
     * 获取活动标签与活动关联详细信息
     *
     * @param activityConnTagId 主键
     */
    @SaCheckPermission("system:activityConnTag:query")
    @GetMapping("/{activityConnTagId}")
    public R<PzcActivityConnTagVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer activityConnTagId) {
        return R.ok(iPzcActivityConnTagService.queryById(activityConnTagId));
    }

    /**
     * 新增活动标签与活动关联
     */
    @SaCheckPermission("system:activityConnTag:add")
    @Log(title = "活动标签与活动关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityConnTagBo bo) {
        return toAjax(iPzcActivityConnTagService.insertByBo(bo));
    }

    /**
     * 修改活动标签与活动关联
     */
    @SaCheckPermission("system:activityConnTag:edit")
    @Log(title = "活动标签与活动关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityConnTagBo bo) {
        return toAjax(iPzcActivityConnTagService.updateByBo(bo));
    }

    /**
     * 删除活动标签与活动关联
     *
     * @param activityConnTagIds 主键串
     */
    @SaCheckPermission("system:activityConnTag:remove")
    @Log(title = "活动标签与活动关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityConnTagIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] activityConnTagIds) {
        return toAjax(iPzcActivityConnTagService.deleteWithValidByIds(Arrays.asList(activityConnTagIds), true));
    }
}
