package com.clipg.exception;

/**
 * @author 77507
 */
public class BusinessException extends RuntimeException{

    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
