package org.litespring.aop.aspectJ;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {

    protected Method adviceMethod;
    protected AspectJExpressionPointcut pointcut;
    protected Object adviceObject;

    public AbstractAspectJAdvice(Method adviceMethod,
                                 AspectJExpressionPointcut pointcut,
                                 Object adviceObject){
        this.adviceMethod = adviceMethod ;
        this.pointcut = pointcut;
        this.adviceObject = adviceObject;
    }
    public void invokeAdviceMethod() throws Throwable{
        adviceMethod.invoke(adviceObject);
        //调用transmanager的start或commit方法
    }
    public Pointcut getPointcut(){
        return this.pointcut;
    }
    public Method getAdviceMethod(){
        return adviceMethod;
    }

}
