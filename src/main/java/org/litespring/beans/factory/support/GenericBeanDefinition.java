package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {
    private String id;
    private String beanClassName;
    private Class<?> beanClass;
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;
    List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;

    }

    public String getBeanClassName() {
        return this.beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    private ConstructorArgument constructorArgument = new ConstructorArgument();


    public boolean isSingleton() {
        return this.singleton;
    }

    public boolean isPrototype() {
        return this.prototype;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;

        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);

        this.prototype = SCOPE_PROTOTYPE.equals(scope);

    }
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    public String getID() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if(className == null)
            return  null;
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
       if(this.beanClass == null){
           throw new IllegalStateException("Bean class name[" + this.getBeanClassName() + "] has not not been resolved into an actual Class");

       }
       return  this.beanClass;
    }

    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

}
