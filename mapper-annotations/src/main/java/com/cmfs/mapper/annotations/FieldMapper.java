package com.cmfs.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段映射
 *
 * @author cmfs
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FieldMapper {

    // 映射的字段名称，为空时使用所注解的字段名
    String value() default "";

//    // 指定的类
//    String targetClass() default "";

}
