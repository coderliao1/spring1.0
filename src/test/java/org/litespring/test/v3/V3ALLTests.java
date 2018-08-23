package org.litespring.test.v3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationContextTestv3.class,
        BeanDefinitionTestv3.class,ConstructorResolverTest.class})
public class V3ALLTests {
}
