package org.litespring.test.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationContextTestv4.class,AutowiredAnnotationProcessorTest.class,
        ClassPathBeanDefinitionScannerTest.class, ClassReaderTest.class,
        DependencyDescriptorTest.class,InjectionMetadataTest.class,
        MetadataReaderTest.class,PackageResourceLoaderTest.class
})
public class V4ALLTests {
}
