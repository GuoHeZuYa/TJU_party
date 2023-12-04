package edu.twt.party.handler;

import com.auth0.jwt.exceptions.JWTDecodeException;
import edu.twt.party.exception.PermissionException;
import edu.twt.party.exception.TokenErrorException;
import edu.twt.party.helper.ResponseCode;
import edu.twt.party.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = {"edu.twt.party.controller"})
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenErrorException.class)
    public ResponseHelper tokenError(TokenErrorException e){
        log.warn("token error",e);
        return ResponseHelper.error(ResponseCode.TOKEN_ERROR);
    }


    @ExceptionHandler(JWTDecodeException.class)
    public ResponseHelper tokenError(JWTDecodeException e){
        log.warn("token error",e);
        return ResponseHelper.error(ResponseCode.TOKEN_ERROR);
    }


    @ExceptionHandler(PermissionException.class)
    public ResponseHelper permissionError(PermissionException e){
        log.warn("no permission",e);
        return ResponseHelper.error(ResponseCode.NO_PERMISSION);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseHelper handleMismatchError(MethodArgumentTypeMismatchException e, HttpServletResponse response){
        log.error("mismatch",e);
        response.setStatus(404);
        return ResponseHelper.error(ResponseCode.SOURCE_NOT_EXIST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseHelper otherError(RuntimeException e){
        log.error("Exception",e);
        return ResponseHelper.error(ResponseCode.ERROR);
    }

}

