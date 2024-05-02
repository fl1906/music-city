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
import top.flya.system.domain.vo.PzcOrganizerTicketVo;
import top.flya.system.domain.bo.PzcOrganizerTicketBo;
import top.flya.system.service.IPzcOrganizerTicketService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 主办方票务
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/organizerTicket")
public class PzcOrganizerTicketController extends BaseController {

    private final IPzcOrganizerTicketService iPzcOrganizerTicketService;

    /**
     * 查询主办方票务列表
     */
    @SaCheckPermission("system:organizerTicket:list")
    @GetMapping("/list")
    public TableDataInfo<PzcOrganizerTicketVo> list(PzcOrganizerTicketBo bo, PageQuery pageQuery) {
        return iPzcOrganizerTicketService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出主办方票务列表
     */
    @SaCheckPermission("system:organizerTicket:export")
    @Log(title = "主办方票务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcOrganizerTicketBo bo, HttpServletResponse response) {
        List<PzcOrganizerTicketVo> list = iPzcOrganizerTicketService.queryList(bo);
        ExcelUtil.exportExcel(list, "主办方票务", PzcOrganizerTicketVo.class, response);
    }

    /**
     * 获取主办方票务详细信息
     *
     * @param organizerTicketId 主键
     */
    @SaCheckPermission("system:organizerTicket:query")
    @GetMapping("/{organizerTicketId}")
    public R<PzcOrganizerTicketVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long organizerTicketId) {
        return R.ok(iPzcOrganizerTicketService.queryById(organizerTicketId));
    }

    /**
     * 新增主办方票务
     */
    @SaCheckPermission("system:organizerTicket:add")
    @Log(title = "主办方票务", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcOrganizerTicketBo bo) {
        return toAjax(iPzcOrganizerTicketService.insertByBo(bo));
    }

    /**
     * 修改主办方票务
     */
    @SaCheckPermission("system:organizerTicket:edit")
    @Log(title = "主办方票务", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcOrganizerTicketBo bo) {
        return toAjax(iPzcOrganizerTicketService.updateByBo(bo));
    }

    /**
     * 删除主办方票务
     *
     * @param organizerTicketIds 主键串
     */
    @SaCheckPermission("system:organizerTicket:remove")
    @Log(title = "主办方票务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{organizerTicketIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] organizerTicketIds) {
        return toAjax(iPzcOrganizerTicketService.deleteWithValidByIds(Arrays.asList(organizerTicketIds), true));
    }
}
