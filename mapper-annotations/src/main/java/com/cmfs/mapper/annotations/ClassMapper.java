package com.cmfs.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类映射。
 * <p>
 * 类名和类对象不能同时为空
 *
 * @author cmfs
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ClassMapper {

    // 映射的完整类名
    String value() default "";

    // 映射的类对象
    Class<?> target() default NONE.class;

    class NONE {
    }

}
