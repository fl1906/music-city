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
import top.flya.system.domain.vo.PzcActivityConnIntroVo;
import top.flya.system.domain.bo.PzcActivityConnIntroBo;
import top.flya.system.service.IPzcActivityConnIntroService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 活动介绍与活动关联
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/activityConnIntro")
public class PzcActivityConnIntroController extends BaseController {

    private final IPzcActivityConnIntroService iPzcActivityConnIntroService;

    /**
     * 查询活动介绍与活动关联列表
     */
    @SaCheckPermission("system:activityConnIntro:list")
    @GetMapping("/list")
    public TableDataInfo<PzcActivityConnIntroVo> list(PzcActivityConnIntroBo bo, PageQuery pageQuery) {
        return iPzcActivityConnIntroService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动介绍与活动关联列表
     */
    @SaCheckPermission("system:activityConnIntro:export")
    @Log(title = "活动介绍与活动关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityConnIntroBo bo, HttpServletResponse response) {
        List<PzcActivityConnIntroVo> list = iPzcActivityConnIntroService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动介绍与活动关联", PzcActivityConnIntroVo.class, response);
    }

    /**
     * 获取活动介绍与活动关联详细信息
     *
     * @param activityConnIntroId 主键
     */
    @SaCheckPermission("system:activityConnIntro:query")
    @GetMapping("/{activityConnIntroId}")
    public R<PzcActivityConnIntroVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer activityConnIntroId) {
        return R.ok(iPzcActivityConnIntroService.queryById(activityConnIntroId));
    }

    /**
     * 新增活动介绍与活动关联
     */
    @SaCheckPermission("system:activityConnIntro:add")
    @Log(title = "活动介绍与活动关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityConnIntroBo bo) {
        return toAjax(iPzcActivityConnIntroService.insertByBo(bo));
    }

    /**
     * 修改活动介绍与活动关联
     */
    @SaCheckPermission("system:activityConnIntro:edit")
    @Log(title = "活动介绍与活动关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityConnIntroBo bo) {
        return toAjax(iPzcActivityConnIntroService.updateByBo(bo));
    }

    /**
     * 删除活动介绍与活动关联
     *
     * @param activityConnIntroIds 主键串
     */
    @SaCheckPermission("system:activityConnIntro:remove")
    @Log(title = "活动介绍与活动关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityConnIntroIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] activityConnIntroIds) {
        return toAjax(iPzcActivityConnIntroService.deleteWithValidByIds(Arrays.asList(activityConnIntroIds), true));
    }
}
