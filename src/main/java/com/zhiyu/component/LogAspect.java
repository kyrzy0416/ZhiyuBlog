package com.zhiyu.component;

import com.zhiyu.domain.SysLog;
import com.zhiyu.service.ISysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 切面获取信息
 */

@Aspect
@Component
public class LogAspect {

    @Autowired
    private ISysLogService sysLogService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zhiyu.controller.*.*(..))")
    public void log() { }

    //表示匹配带有自定义注解的方法
    @Pointcut("@annotation(com.zhiyu.component.Log)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result =null;
        long beginTime = System.currentTimeMillis();

        try {
//            logger.info("我在目标方法之前执行！");
            result = point.proceed();
            long endTime = System.currentTimeMillis();
            insertLog(point,endTime-beginTime);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
        }
        return result;
    }

    // 用户操作记录
    private void insertLog(ProceedingJoinPoint point , long time) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        SysLog sys_log = new SysLog();

        Log userAction = method.getAnnotation(Log.class);
        if (userAction != null) {
            // 注解上的描述
            sys_log.setAction(userAction.value());
        }

        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = signature.getName();
        // 请求的方法参数值
        String args = Arrays.toString(point.getArgs());

        //从session中获取当前登陆人id
//		Long useride = (Long)SecurityUtils.getSubject().getSession().getAttribute("userid");

        Long userid = 1L;//应该从session中获取当前登录人的id，这里简单模拟下

        sys_log.setUserId(userid);

        sys_log.setTime(new java.sql.Timestamp(new Date().getTime()));

        // 下面这句会报错，待解决
        //logger.info("当前登陆人：{},类名:{},方法名:{},参数：{},执行时间：{}",userid, className, methodName, args, time);

        sysLogService.insertLog(sys_log);
    }

    @Before("log()")
    public void doBefore(JoinPoint joinpoint) {
        // 获取信息（ip地址、网页地址）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName();
        Object[] args = joinpoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
//        logger.info("-------------doAfter-------------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "Resultlog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
