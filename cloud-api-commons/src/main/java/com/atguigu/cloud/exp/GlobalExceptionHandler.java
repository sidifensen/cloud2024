package com.atguigu.cloud.exp;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auther zzyy
 * @create 2023-11-04 12:20
 */
@Slf4j
@RestControllerAdvice//控制器增强器，可以捕获控制器抛出的异常并进行处理。
public class GlobalExceptionHandler
{
    /**
     * 默认全局异常处理。
     * @param e the e
     * @return ResultData
     */
    @ExceptionHandler(RuntimeException.class)//捕获所有运行时异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//设置响应状态码
    public ResultData<String> exception(Exception e) {
        System.out.println("----come in GlobalExceptionHandler");
        log.error("全局异常信息exception:{}", e.getMessage(), e);//记录异常日志
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
    }
}
































