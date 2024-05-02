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
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.bo.PzcActivityBo;
import top.flya.system.domain.vo.PzcActivityVo;
import top.flya.system.service.IPzcActivityService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 活动
 *
 * @author ruoyi
 * @date 2023-06-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/activity")
@Slf4j
public class PzcActivityController extends BaseController {

    private final IPzcActivityService iPzcActivityService;

    /**
     * 新增活动
     */
    @SaCheckPermission("system:activity:add")
    @Log(title = "活动", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcActivityBo bo) {
        return toAjax(iPzcActivityService.insertByBo(bo));
    }

    /**
     * 修改活动
     */
    @SaCheckPermission("system:activity:edit")
    @Log(title = "活动", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcActivityBo bo) {
        return toAjax(iPzcActivityService.updateByBo(bo));
    }




    /**
     * 查询活动列表
     */
    @SaCheckPermission("system:activity:list")
    @GetMapping("/list")
    public TableDataInfo<PzcActivityVo> list(PzcActivityBo bo, PageQuery pageQuery) {
        return iPzcActivityService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询活动列表 小程序端
     */
    @GetMapping("/listWx")
    public TableDataInfo<PzcActivityVo> Wx(PzcActivityBo bo, PageQuery pageQuery) {
        pageQuery.setIsAsc("desc");
        pageQuery.setOrderByColumn("start_time");
        return iPzcActivityService.queryPageListWx(bo, pageQuery);
    }

    /**
     * 导出活动列表
     */
    @SaCheckPermission("system:activity:export")
    @Log(title = "活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcActivityBo bo, HttpServletResponse response) {
        List<PzcActivityVo> list = iPzcActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动", PzcActivityVo.class, response);
    }

    /**
     * 获取活动详细信息
     *
     * @param activityId 主键
     */
    @GetMapping("/{activityId}")
    public R<PzcActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer activityId) {
        return R.ok(iPzcActivityService.queryById(activityId));
    }





    /**
     * 删除活动
     *
     * @param activityIds 主键串
     */
    @SaCheckPermission("system:activity:remove")
    @Log(title = "活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] activityIds) {
        return toAjax(iPzcActivityService.deleteWithValidByIds(Arrays.asList(activityIds), true));
    }
}
