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
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.bo.PzcUserBo;
import top.flya.system.domain.bo.UpdateMoneyBo;
import top.flya.system.domain.vo.PzcUserVo;
import top.flya.system.service.IPzcUserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/pzc_user")
public class PzcUserController extends BaseController {

    private final IPzcUserService iPzcUserService;




    /**
     * 查询用户列表
     */
    @SaCheckPermission("system:pzc_user:list")
    @GetMapping("/list")
    public TableDataInfo<PzcUserVo> list(PzcUserBo bo, PageQuery pageQuery) {
        return iPzcUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("system:pzc_user:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcUserBo bo, HttpServletResponse response) {
        List<PzcUserVo> list = iPzcUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", PzcUserVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param userId 主键
     */
    @SaCheckPermission("system:pzc_user:query")
    @GetMapping("/{userId}")
    public R<PzcUserVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long userId) {
        return R.ok(iPzcUserService.queryById(userId));
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("system:pzc_user:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcUserBo bo) {
        return toAjax(iPzcUserService.insertByBo(bo));
    }


    @PostMapping("/updateMoney")
    public R<Void> updateMoney(@Validated(AddGroup.class) @RequestBody UpdateMoneyBo bo) {
        return toAjax(iPzcUserService.updateMoney(bo));
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("system:pzc_user:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcUserBo bo) {
        return toAjax(iPzcUserService.updateByBo(bo));
    }

    /**
     * 删除用户
     *
     * @param userIds 主键串
     */
    @SaCheckPermission("system:pzc_user:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] userIds) {
        return toAjax(iPzcUserService.deleteWithValidByIds(Arrays.asList(userIds), true));
    }
}
