package edu.twt.party.exception;

import edu.twt.party.helper.ResponseCode;
import lombok.Getter;

@Getter
public class APIException extends RuntimeException {
    private ResponseCode code;
    private String msg;

    public APIException() {
        this( "接口错误");
    }

    public APIException(String msg) {
        this(ResponseCode.FAILED, msg);
    }

    public APIException(ResponseCode code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
