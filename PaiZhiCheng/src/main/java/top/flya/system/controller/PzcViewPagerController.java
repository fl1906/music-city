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
import top.flya.system.domain.bo.PzcViewPagerBo;
import top.flya.system.domain.vo.PzcViewPagerVo;
import top.flya.system.service.IPzcViewPagerService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 轮播图
 *
 * @author ruoyi
 * @date 2023-05-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/viewPager")
public class PzcViewPagerController extends BaseController {

    private final IPzcViewPagerService iPzcViewPagerService;

    /**
     * 查询轮播图列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcViewPagerVo> list(PzcViewPagerBo bo, PageQuery pageQuery) {
        return iPzcViewPagerService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出轮播图列表
     */
    @SaCheckPermission("system:viewPager:export")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcViewPagerBo bo, HttpServletResponse response) {
        List<PzcViewPagerVo> list = iPzcViewPagerService.queryList(bo);
        ExcelUtil.exportExcel(list, "轮播图", PzcViewPagerVo.class, response);
    }

    /**
     * 获取轮播图详细信息
     *
     * @param viewPagerId 主键
     */
    @GetMapping("/{viewPagerId}")
    public R<PzcViewPagerVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Integer viewPagerId) {
        return R.ok(iPzcViewPagerService.queryById(viewPagerId));
    }

    /**
     * 新增轮播图
     */
    @SaCheckPermission("system:viewPager:add")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcViewPagerBo bo) {
        return toAjax(iPzcViewPagerService.insertByBo(bo));
    }

    /**
     * 修改轮播图
     */
    @SaCheckPermission("system:viewPager:edit")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcViewPagerBo bo) {
        return toAjax(iPzcViewPagerService.updateByBo(bo));
    }

    /**
     * 删除轮播图
     *
     * @param viewPagerIds 主键串
     */
    @SaCheckPermission("system:viewPager:remove")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{viewPagerIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Integer[] viewPagerIds) {
        return toAjax(iPzcViewPagerService.deleteWithValidByIds(Arrays.asList(viewPagerIds), true));
    }
}
