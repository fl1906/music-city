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
import top.flya.system.domain.vo.PzcTagVo;
import top.flya.system.domain.bo.PzcTagBo;
import top.flya.system.service.IPzcTagService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 活动标签
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/tag")
public class PzcTagController extends BaseController {

    private final IPzcTagService iPzcTagService;

    /**
     * 查询活动标签列表
     */
    @SaCheckPermission("system:tag:list")
    @GetMapping("/list")
    public TableDataInfo<PzcTagVo> list(PzcTagBo bo, PageQuery pageQuery) {
        return iPzcTagService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动标签列表
     */
    @SaCheckPermission("system:tag:export")
    @Log(title = "活动标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcTagBo bo, HttpServletResponse response) {
        List<PzcTagVo> list = iPzcTagService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动标签", PzcTagVo.class, response);
    }

    /**
     * 获取活动标签详细信息
     *
     * @param tagId 主键
     */
    @SaCheckPermission("system:tag:query")
    @GetMapping("/{tagId}")
    public R<PzcTagVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long tagId) {
        return R.ok(iPzcTagService.queryById(tagId));
    }

    /**
     * 新增活动标签
     */
    @SaCheckPermission("system:tag:add")
    @Log(title = "活动标签", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcTagBo bo) {
        return toAjax(iPzcTagService.insertByBo(bo));
    }

    /**
     * 修改活动标签
     */
    @SaCheckPermission("system:tag:edit")
    @Log(title = "活动标签", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcTagBo bo) {
        return toAjax(iPzcTagService.updateByBo(bo));
    }

    /**
     * 删除活动标签
     *
     * @param tagIds 主键串
     */
    @SaCheckPermission("system:tag:remove")
    @Log(title = "活动标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tagIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] tagIds) {
        return toAjax(iPzcTagService.deleteWithValidByIds(Arrays.asList(tagIds), true));
    }
}
