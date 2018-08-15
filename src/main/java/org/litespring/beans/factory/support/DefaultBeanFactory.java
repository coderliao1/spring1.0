package org.litespring.beans.factory.support;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.factory.BeanCreationException;

import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
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

        //创建实例
        Object bean = instantiateBean(bd);
        //设置属性
        populateBean(bd, bean);

        return bean;

    }

    protected void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> pvs = bd.getPropertyValues();

        if (pvs ==null || pvs.isEmpty()){
            return;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);

        SimpleTypeConverter converter = new SimpleTypeConverter();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for(PropertyValue pv :pvs){
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);


                //使用Introspector，获取bean的信息
                for (PropertyDescriptor pd :pds){
                    if (pd.getName().equals(propertyName)){
                        Object convertedValue = converter.convertIfNecessary(resolvedValue,pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean , convertedValue);
                        //相当于set方法
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", e);
        }
    }

    private Object instantiateBean(BeanDefinition bd) {
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
