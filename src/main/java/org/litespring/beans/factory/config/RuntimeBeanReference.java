package org.litespring.beans.factory.config;

public class RuntimeBeanReference {
    private final String BeanName;

    public RuntimeBeanReference(String beanName) {
        BeanName = beanName;
    }
    public String getBeanName(){
        return  this.BeanName;
    }
}
