package com.buyi.spzx.common.exception;

import com.buyi.spzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException{
    private Integer code;
    private String message;
    private ResultCodeEnum resultCodeEnum;

    public MyException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
        this.resultCodeEnum = resultCodeEnum;
    }
}
