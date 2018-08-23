package org.litespring.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
//可以写在类上面
@Retention(RetentionPolicy.RUNTIME)
//保留在运行时
@Documented
//可以写道javadoc文件里
public @interface Component {
    /**

     * The value may indicate a suggestion for a logical component name,

     * to be turned into a Spring bean in case of an autodetected component.

     * @return the suggested component name, if any

     */

    String value() default "";
}
