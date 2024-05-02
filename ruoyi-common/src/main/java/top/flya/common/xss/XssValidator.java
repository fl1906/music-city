package top.flya.common.xss;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义xss校验注解实现
 *
 * @author Lion Li
 */
@Slf4j
public class XssValidator implements ConstraintValidator<Xss, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        log.info("xss validator: {}", value);
        return !ReUtil.contains(HtmlUtil.RE_HTML_MARK, value);
    }

}
