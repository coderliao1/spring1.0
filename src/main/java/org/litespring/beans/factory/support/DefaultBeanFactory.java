package org.litespring.beans.factory.support;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;

import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory,BeanDefinitionRegistry {

    private final Map<String,BeanDefinition> beanDefinationMap = new ConcurrentHashMap<String, BeanDefinition>();
    private   ClassLoader beanClassLoader;
     public DefaultBeanFactory(){

     }

    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
     this.beanDefinationMap.put(beanID,bd);
    }
    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinationMap.get(beanID);
    }

    public Object getBean(String beanID) {
        BeanDefinition bd = this.getBeanDefinition(beanID);
        if (bd==null){
            return null;
        }
        if (bd.isSingleton()){
            Object bean = this.getSingleton(beanID);
            if(bean == null){
                bean = createBean(bd);
                this.registerSingleton(beanID,bean);
            }
            return bean;
        }

        return createBean(bd);
    }

    private Object createBean(BeanDefinition bd) {
        ClassLoader c1 = this.getBeanClassLoader();
        String beanClassName = bd.getBeanClassName();

        try {
            Class<?>  clz = c1.loadClass(beanClassName);
            return clz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for"+beanClassName+"error");
        }
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return  (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }
}
