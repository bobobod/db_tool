package com.cczu.db_tool.exception;

/**
 * @author jianzhen.yin
 * @date 2020/9/10
 */
public class BusinessException extends RuntimeException {
    private String message;
    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
