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
import top.flya.common.helper.LoginHelper;
import top.flya.common.utils.poi.ExcelUtil;
import top.flya.system.domain.vo.PzcUserPhotoVo;
import top.flya.system.domain.bo.PzcUserPhotoBo;
import top.flya.system.service.IPzcUserPhotoService;
import top.flya.common.core.page.TableDataInfo;

/**
 * 用户资料相册
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/userPhoto")
public class PzcUserPhotoController extends BaseController {

    private final IPzcUserPhotoService iPzcUserPhotoService;

    /**
     * 查询用户资料相册列表
     */
    @GetMapping("/list")
    public TableDataInfo<PzcUserPhotoVo> list(PzcUserPhotoBo bo, PageQuery pageQuery) {
        if(bo.getUserId()==null)
        {
            bo.setUserId(LoginHelper.getUserId());
        }
        return iPzcUserPhotoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户资料相册列表
     */
    @Log(title = "用户资料相册", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PzcUserPhotoBo bo, HttpServletResponse response) {
        List<PzcUserPhotoVo> list = iPzcUserPhotoService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户资料相册", PzcUserPhotoVo.class, response);
    }

    /**
     * 获取用户资料相册详细信息
     *
     * @param photoId 主键
     */
    @GetMapping("/{photoId}")
    public R<PzcUserPhotoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long photoId) {
        return R.ok(iPzcUserPhotoService.queryById(photoId));
    }

    /**
     * 新增用户资料相册
     */
    @Log(title = "用户资料相册", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PzcUserPhotoBo bo) {
        bo.setUserId(LoginHelper.getUserId());
        return toAjax(iPzcUserPhotoService.insertByBo(bo));
    }

    /**
     * 修改用户资料相册
     */
    @Log(title = "用户资料相册", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PzcUserPhotoBo bo) {
        return toAjax(iPzcUserPhotoService.updateByBo(bo));
    }

    /**
     * 删除用户资料相册
     *
     * @param photoIds 主键串
     */
    @Log(title = "用户资料相册", businessType = BusinessType.DELETE)
    @DeleteMapping("/{photoIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] photoIds) {
        return toAjax(iPzcUserPhotoService.deleteWithValidByIds(Arrays.asList(photoIds), true));
    }
}
