package com.buyi.spzx.common.exception;

import com.buyi.spzx.model.vo.common.Result;
import com.buyi.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public Result<String> MyError(MyException e) {
        return Result.error(e.getResultCodeEnum());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> SysError() {
        return Result.error(ResultCodeEnum.SYSTEM_ERROR);
    }
}
