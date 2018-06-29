package org.litespring.beans;

public interface BeanDefinition {
     static final String SCOPE_SINGLETON = "singleton";
     static final String SCOPE_PROTOTYPE = "prototype";
    static final String SCOPE_DEFAULT = "";
     boolean isSingleton();
     boolean isPrototype();
     String getScope();
     void  setScope(String scope);
     String getBeanClassName();
}
