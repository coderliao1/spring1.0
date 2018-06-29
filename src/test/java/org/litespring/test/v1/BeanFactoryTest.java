package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.*;

public class BeanFactoryTest {
    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader reader = null;
    @Before
    public void init(){
         factory = new DefaultBeanFactory();
         reader = new XmlBeanDefinitionReader(factory);

    }


    @Test
    public void testGetBean(){

        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        assertTrue(bd.isSingleton());

        assertFalse(bd.isPrototype());

        assertEquals(BeanDefinition.SCOPE_DEFAULT,bd.getScope());

        assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());


        PetStoreService petStoreService1 = (PetStoreService) factory.getBean("petStore");

        assertNotNull(petStoreService1);
    }

     @Test
    public void testInvalidBean(){
         reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));

        try {
            factory.getBean("invalidBean");
        }catch (BeanCreationException exception){
            return;
        }
            Assert.fail("expect BeanCreationException");
    }
    @Test
    public void testInvalidXML(){

        try{
            reader.loadBeanDefinitions(new ClassPathResource("xxx.xml"));
        }catch (BeanDefinitionStoreException e){
            return;
        }
        Assert.fail("expect BeanDefinitionStoreException");
    }
}
