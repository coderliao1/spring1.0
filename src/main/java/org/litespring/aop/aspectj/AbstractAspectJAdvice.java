package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {

    protected Method adviceMethod;
    protected AspectJExpressionPointcut pointcut;
    protected AspectInstanceFactory adviceObjectFactory;

    public AbstractAspectJAdvice(Method adviceMethod,
                                 AspectJExpressionPointcut pointcut,
                                 AspectInstanceFactory adviceObjectFactory){
        this.adviceMethod = adviceMethod ;
        this.pointcut = pointcut;
        this.adviceObjectFactory = adviceObjectFactory;
    }
    public void invokeAdviceMethod() throws Throwable{
        adviceMethod.invoke(adviceObjectFactory.getAspectInstance());
        //调用transmanager的start或commit方法
    }
    public Pointcut getPointcut(){
        return this.pointcut;
    }
    public Method getAdviceMethod(){
        return adviceMethod;
    }
    public Object getAdviceInstance() throws Exception {

        return adviceObjectFactory.getAspectInstance();

    }

}
