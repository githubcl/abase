package cn.edu.cqut.base.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来处理ItemAdapter中显示字段的注解
 * 
 * @author chenliang
 * @version v1.1
 * @date 2014-2-23
 * 
 */
// 这里必须添加@Retention(RetentionPolicy.RUNTIME)，不然程序运行过程中获取不到注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
// 只能将注解加到方法上
public @interface ItemAnnotation
{
	ItemType[] types() default {};

	String[] adapters() default {};
}
