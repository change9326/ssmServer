package com.ssm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Title: CustomExceptionResolver</p>
 * <p>Description:全局异常处理器 </p>
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

    /**
     * （非 Javadoc）
     * <p>Title: resolveException</p>
     * <p>Description: </p>
     *
     * @param request
     * @param response
     * @param handler
     * @param ex       系统 抛出的异常
     * @return
     * @see HandlerExceptionResolver#resolveException(HttpServletRequest, HttpServletResponse, Object, Exception)
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        //handler就是处理器适配器要执行Handler对象（只有method）

//		解析出异常类型
//		如果该 异常类型是系统 自定义的异常，直接取出异常信息，在错误页面展示
//		String message = null;
//		if(ex instanceof CustomException){
//			message = ((CustomException)ex).getMessage();
//		}else{
////			如果该 异常类型不是系统 自定义的异常，构造一个自定义的异常类型（信息为“未知错误”）
//			message="未知错误";
//		}

        //上边代码变为
   /*     CustomException customException = null;
        if (ex instanceof CustomException) {
            customException = (CustomException) ex;
        } else {
            customException = new CustomException("未知错误");
        }

        //错误信息
        String message = customException.getMessage();


        ModelAndView modelAndView = new ModelAndView();

        //将错误信息传到页面
        modelAndView.addObject("message", message);

        //指向错误页面
        modelAndView.setViewName("error");


        return modelAndView;*/

        ModelAndView modelAndView = new ModelAndView();

        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");

        CustomException customException = null;
        if (ex instanceof CustomException) {
            customException = (CustomException) ex;
        } else {
            customException = new CustomException("未知错误");
        }
        try {
            response.getWriter().write("{\"res\":-1,\"msg\":\"" + customException.getMessage() + "\"}");
        } catch (IOException e) {

        }
        return modelAndView;
    }
}
