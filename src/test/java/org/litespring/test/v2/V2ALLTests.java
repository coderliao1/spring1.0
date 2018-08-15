package org.litespring.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationContextTestv2.class,
        BeanDefinitionTestv2.class,BeanDefinitionValueResolverTest.class,
        CustomBooleanEditorTest.class,CustomNumberEditorTest.class,
        TypeConverterTest.class})
public class V2ALLTests {
}
