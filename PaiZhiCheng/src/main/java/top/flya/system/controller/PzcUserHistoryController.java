package top.flya.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
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
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.bo.PzcUserHistoryBo;
import top.flya.system.domain.vo.PzcUserHistoryVo;
import top.flya.system.service.IPzcUserHistoryService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户操作历史记录
 *
 * @author ruoyi
 * @date 2023-07-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/userHistory")
public class PzcUserHistoryController extends BaseController {

    private final IPzcUserHistoryService iPzcUserHistoryService;

    /**
     * 查询用户操作历史记录列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcUserHistoryVo> list(PzcUserHistoryBo bo, PageQuery pageQuery) {
        bo.setUserId(LoginHelper.getUserId());
        pageQuery.setOrderByColumn("create_time");
        pageQuery.setIsAsc("desc");
        return iPzcUserHistoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户操作历史记录列表
     */
    @SaCheckPermission("system:userHistory:export")
    @Log(title = "用户操作历史记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcUserHistoryBo bo, HttpServletResponse response) {
        List<PzcUserHistoryVo> list = iPzcUserHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户操作历史记录", PzcUserHistoryVo.class, response);
    }

    /**
     * 获取用户操作历史记录详细信息
     *
     * @param historyId 主键
     */
    @SaCheckPermission("system:userHistory:query")
    @GetMapping("/{historyId}")
    public R<PzcUserHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long historyId) {
        return R.ok(iPzcUserHistoryService.queryById(historyId));
    }

    /**
     * 新增用户操作历史记录
     */
    @SaCheckPermission("system:userHistory:add")
    @Log(title = "用户操作历史记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcUserHistoryBo bo) {
        return toAjax(iPzcUserHistoryService.insertByBo(bo));
    }

    /**
     * 修改用户操作历史记录
     */
    @SaCheckPermission("system:userHistory:edit")
    @Log(title = "用户操作历史记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcUserHistoryBo bo) {
        return toAjax(iPzcUserHistoryService.updateByBo(bo));
    }

    /**
     * 删除用户操作历史记录
     *
     * @param historyIds 主键串
     */
    @SaCheckPermission("system:userHistory:remove")
    @Log(title = "用户操作历史记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{historyIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] historyIds) {
        return toAjax(iPzcUserHistoryService.deleteWithValidByIds(Arrays.asList(historyIds), true));
    }
}
