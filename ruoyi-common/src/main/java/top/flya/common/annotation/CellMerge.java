package top.flya.common.annotation;

import top.flya.common.excel.CellMergeStrategy;

import java.lang.annotation.*;

/**
 * excel 列单元格合并(合并列相同项)
 *
 * 需搭配 {@link CellMergeStrategy} 策略使用
 *
 * @author Lion Li
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellMerge {

	/**
	 * col index
	 */
	int index() default -1;

}
