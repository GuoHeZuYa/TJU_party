package edu.twt.party.exception;

import edu.twt.party.helper.ResponseCode;
import org.apache.log4j.spi.ErrorCode;

/**
 * @ClassName: TokenErrorException
 * @Description:
 * @Author: 过河卒
 * @Date: 2022/9/7 14:57
 * @Version: 1.0
 */
public class TokenErrorException extends RuntimeException {
    private ResponseCode error;
    public TokenErrorException(ResponseCode error){
        super(error.getMsg());
        this.error = error;
    }
    public ResponseCode getError() {
        return error;
    }
}
