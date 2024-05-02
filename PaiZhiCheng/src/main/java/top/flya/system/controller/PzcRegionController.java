package top.flya.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.flya.common.annotation.Log;
import top.flya.common.annotation.RepeatSubmit;
import top.flya.common.core.controller.BaseController;
import top.flya.common.core.domain.R;
import top.flya.common.core.validate.AddGroup;
import top.flya.common.core.validate.EditGroup;
import top.flya.common.enums.BusinessType;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.bo.PzcRegionBo;
import top.flya.system.domain.vo.PzcRegionVo;
import top.flya.system.mapper.RegionTreeMapper;
import top.flya.system.service.IPzcRegionService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 地区
 *
 * @author ruoyi
 * @date 2023-07-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/region")
public class PzcRegionController extends BaseController {

    private final IPzcRegionService iPzcRegionService;

    private final RegionTreeMapper regionTreeMapper;

//    /**
//     * 查询地区列表
//     */
//    @SaCheckPermission("system:region:list")
//    @GetMapping("/list")
//    public TableDataInfo<PzcRegionVo> list(PzcRegionBo bo, PageQuery pageQuery) {
//        return iPzcRegionService.queryPageList(bo, pageQuery);
//    }


    @GetMapping("/list")
    public R list(@RequestParam(required = false) String regionName) {
        PzcRegionBo bo = new PzcRegionBo();
        bo.setName(regionName);
        List<PzcRegionVo> pzcRegionVos = iPzcRegionService.queryList(bo);
        //根据 pzcRegionVos 生成树形结构 base 字段相同的为同一级
        return R.ok(regionTreeMapper.buildTree(pzcRegionVos));
    }

    /**
     * 导出地区列表
     */
    @SaCheckPermission("system:region:export")
    @Log(title = "地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcRegionBo bo, HttpServletResponse response) {
        List<PzcRegionVo> list = iPzcRegionService.queryList(bo);
        ExcelUtil.exportExcel(list, "地区", PzcRegionVo.class, response);
    }

    /**
     * 获取地区详细信息
     *
     * @param regionId 主键
     */
    @SaCheckPermission("system:region:query")
    @GetMapping("/{regionId}")
    public R<PzcRegionVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long regionId) {
        return R.ok(iPzcRegionService.queryById(regionId));
    }

    /**
     * 新增地区
     */
    @SaCheckPermission("system:region:add")
    @Log(title = "地区", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcRegionBo bo) {
        return toAjax(iPzcRegionService.insertByBo(bo));
    }

    /**
     * 修改地区
     */
    @SaCheckPermission("system:region:edit")
    @Log(title = "地区", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcRegionBo bo) {
        return toAjax(iPzcRegionService.updateByBo(bo));
    }

    /**
     * 删除地区
     *
     * @param regionIds 主键串
     */
    @SaCheckPermission("system:region:remove")
    @Log(title = "地区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regionIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] regionIds) {
        return toAjax(iPzcRegionService.deleteWithValidByIds(Arrays.asList(regionIds), true));
    }
}
