package top.flya.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.annotation.Log;
import top.flya.common.annotation.RepeatSubmit;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.PageQuery;
import top.flya.common.core.domain.R;
import top.flya.common.core.page.TableDataInfo;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
import top.flya.common.enums.BusinessType;
import top.flya.common.utils.JsonUtils;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.bo.PzcIntroBo;
import top.flya.system.domain.vo.PzcIntroVo;
import top.flya.system.service.IPzcIntroService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 活动介绍
 *
 * @author ruoyi
 * @date 2023-08-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/system/intro")
public class PzcIntroController extends BaseController {

    private final IPzcIntroService iPzcIntroService;

    /**
     * 查询活动介绍列表
     */
    @SaCheckPermission("system:intro:list")
    @GetMapping("/list")
    public TableDataInfo<PzcIntroVo> list(PzcIntroBo bo, PageQuery pageQuery) {
        return iPzcIntroService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动介绍列表
     */
    @SaCheckPermission("system:intro:export")
    @Log(title = "活动介绍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcIntroBo bo, HttpServletResponse response) {
        List<PzcIntroVo> list = iPzcIntroService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动介绍", PzcIntroVo.class, response);
    }

    /**
     * 获取活动介绍详细信息
     *
     * @param introId 主键
     */
    @SaCheckPermission("system:intro:query")
    @GetMapping("/{introId}")
    public R<PzcIntroVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long introId) {
        return R.ok(iPzcIntroService.queryById(introId));
    }

    /**
     * 新增活动介绍
     */
    @SaCheckPermission("system:intro:add")
    @Log(title = "活动介绍", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcIntroBo bo) {
        return toAjax(iPzcIntroService.insertByBo(bo));
    }

    /**
     * 修改活动介绍
     */
    @SaCheckPermission("system:intro:edit")
    @Log(title = "活动介绍", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcIntroBo bo) {
        log.info("bo:{}", JsonUtils.toJsonString(bo));
        return toAjax(iPzcIntroService.updateByBo(bo));
    }

    /**
     * 删除活动介绍
     *
     * @param introIds 主键串
     */
    @SaCheckPermission("system:intro:remove")
    @Log(title = "活动介绍", businessType = BusinessType.DELETE)
    @DeleteMapping("/{introIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] introIds) {
        return toAjax(iPzcIntroService.deleteWithValidByIds(Arrays.asList(introIds), true));
    }
}
