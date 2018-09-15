package org.litespring.beans.factory;

import org.litespring.aop.Advice;
import org.litespring.beans.BeanDefinition;

import java.util.List;

public interface BeanFactory {
   // BeanDefinition getBeanDefinition(String beanID);
    //由BeanDefinitionRegistry实现
    Object getBean(String beanID);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);
}
