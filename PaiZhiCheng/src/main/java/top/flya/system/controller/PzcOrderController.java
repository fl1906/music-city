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
import top.flya.system.domain.vo.PzcOrderVo;
import top.flya.system.domain.bo.PzcOrderBo;
import top.flya.system.service.IPzcOrderService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 订单
 *
 * @author ruoyi
 * @date 2023-07-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/pzc_order")
public class PzcOrderController extends BaseController {

    private final IPzcOrderService iPzcOrderService;

    /**
     * 查询订单列表
     */
    @SaCheckPermission("system:pzc_order:list")
    @GetMapping("/list")
    public TableDataInfo<PzcOrderVo> list(PzcOrderBo bo, PageQuery pageQuery) {
        return iPzcOrderService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单列表
     */
    @SaCheckPermission("system:pzc_order:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcOrderBo bo, HttpServletResponse response) {
        List<PzcOrderVo> list = iPzcOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单", PzcOrderVo.class, response);
    }

    /**
     * 获取订单详细信息
     *
     * @param orderId 主键
     */
    @SaCheckPermission("system:pzc_order:query")
    @GetMapping("/{orderId}")
    public R<PzcOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long orderId) {
        return R.ok(iPzcOrderService.queryById(orderId));
    }

    /**
     * 新增订单
     */
    @SaCheckPermission("system:pzc_order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcOrderBo bo) {
        return toAjax(iPzcOrderService.insertByBo(bo));
    }

    /**
     * 修改订单
     */
    @SaCheckPermission("system:pzc_order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcOrderBo bo) {
        return toAjax(iPzcOrderService.updateByBo(bo));
    }

    /**
     * 删除订单
     *
     * @param orderIds 主键串
     */
    @SaCheckPermission("system:pzc_order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] orderIds) {
        return toAjax(iPzcOrderService.deleteWithValidByIds(Arrays.asList(orderIds), true));
    }
}
