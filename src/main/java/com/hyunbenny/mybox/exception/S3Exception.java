package com.hyunbenny.mybox.exception;

public class S3Exception extends CustomException{

    private static final String MESSAGE = "파일 저장 중 오류가 발생했습니다.";

    public S3Exception() {
        super(MESSAGE);
    }

    public S3Exception(String message) {
        super(MESSAGE);
    }

    public S3Exception(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
