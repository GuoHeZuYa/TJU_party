package edu.twt.party.exception;

import edu.twt.party.helper.ResponseCode;
import lombok.AllArgsConstructor;


public class PermissionException extends RuntimeException{
    private ResponseCode error;

    public PermissionException(ResponseCode error){
        super(error.getMsg());
        this.error = error;
    }

    public ResponseCode getError() {
        return error;
    }
}
