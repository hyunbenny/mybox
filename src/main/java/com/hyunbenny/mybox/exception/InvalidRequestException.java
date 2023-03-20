package com.hyunbenny.mybox.exception;

public class InvalidRequestException extends CustomException{

        private static final String MESSAGE = "잘못된 요청입니다.";

        public InvalidRequestException() {
            super(MESSAGE);
        }

        public InvalidRequestException(String fieldName, String message) {
            super(MESSAGE);
            addValidation(fieldName, message);
        }

        public InvalidRequestException(Throwable cause) {
            super(MESSAGE, cause);
        }

        @Override
        public int getStatusCode() {
            return 400;
        }

}
