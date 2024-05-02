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
import top.flya.system.domain.vo.PzcArtistVo;
import top.flya.system.domain.bo.PzcArtistBo;
import top.flya.system.service.IPzcArtistService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 艺人
 *
 * @author flya
 * @date 2023-06-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/artist")
public class PzcArtistController extends BaseController {

    private final IPzcArtistService iPzcArtistService;

    /**
     * 查询艺人列表
     */
    @SaCheckPermission("system:artist:list")
    @GetMapping("/list")
    public TableDataInfo<PzcArtistVo> list(PzcArtistBo bo, PageQuery pageQuery) {
        return iPzcArtistService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出艺人列表
     */
    @SaCheckPermission("system:artist:export")
    @Log(title = "艺人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcArtistBo bo, HttpServletResponse response) {
        List<PzcArtistVo> list = iPzcArtistService.queryList(bo);
        ExcelUtil.exportExcel(list, "艺人", PzcArtistVo.class, response);
    }

    /**
     * 获取艺人详细信息
     *
     * @param artistId 主键
     */
    @SaCheckPermission("system:artist:query")
    @GetMapping("/{artistId}")
    public R<PzcArtistVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long artistId) {
        return R.ok(iPzcArtistService.queryById(artistId));
    }

    /**
     * 新增艺人
     */
    @SaCheckPermission("system:artist:add")
    @Log(title = "艺人", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcArtistBo bo) {
        return toAjax(iPzcArtistService.insertByBo(bo));
    }

    /**
     * 修改艺人
     */
    @SaCheckPermission("system:artist:edit")
    @Log(title = "艺人", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcArtistBo bo) {
        return toAjax(iPzcArtistService.updateByBo(bo));
    }

    /**
     * 删除艺人
     *
     * @param artistIds 主键串
     */
    @SaCheckPermission("system:artist:remove")
    @Log(title = "艺人", businessType = BusinessType.DELETE)
    @DeleteMapping("/{artistIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] artistIds) {
        return toAjax(iPzcArtistService.deleteWithValidByIds(Arrays.asList(artistIds), true));
    }
}
