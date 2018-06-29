package org.litespring.beans.factory;

import org.litespring.beans.BeanDefinition;

public interface BeanFactory {
   // BeanDefinition getBeanDefinition(String beanID);
    //由BeanDefinitionRegistry实现
    Object getBean(String beanID);
}
