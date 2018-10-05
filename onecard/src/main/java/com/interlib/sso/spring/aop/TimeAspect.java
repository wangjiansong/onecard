package com.interlib.sso.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计算执行时间消耗的切面类
 * @author zhoulonghuan
 */
@Aspect
public class TimeAspect {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
    @Pointcut("execution(* com.interlib.opac.service.*.*(..))")
    public void timeCostCall() { }
    
    //TODO 没法执行
    @Pointcut("execution(* com.interlib.opac.servlet.tags.*.*(..))")
    public void servletTagsPoint() { }
    
	@Around(value = "timeCostCall()", argNames = "rtv")
	public Object timeCostCallCalls(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		// 环绕通知处理方法
		try {
			long start = System.currentTimeMillis();
			result = pjp.proceed();
			long end = System.currentTimeMillis();
			long costTimeInMills = end - start;
			String timeInfo = costTimeInMills > 1000 ? "[" + costTimeInMills + 
					"]" : costTimeInMills + "";
			String className = pjp.getTarget().getClass().getName();
			String methodName = pjp.getSignature().getName();
			log.info(className + "." + methodName + " execute cost: " + 
					timeInfo + " ms.");
		} catch (Exception e) {
			log.error("time cost aspect run error.", e);
		}
		return result;
	}
}
