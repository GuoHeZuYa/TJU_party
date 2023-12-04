//package edu.twt.party.advice;
//
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.auth0.jwt.exceptions.SignatureVerificationException;
//import edu.twt.party.exception.APIException;
//import edu.twt.party.helper.ResponseCode;
//import edu.twt.party.helper.ResponseHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.multipart.support.MissingServletRequestPartException;
//
//import javax.validation.ConstraintViolationException;
//import java.sql.SQLIntegrityConstraintViolationException;
//
//@RestControllerAdvice(basePackages = {"edu.twt.party.controller"})
//@Slf4j
//public class ExceptionAdvice {
//    @ExceptionHandler(APIException.class)
//    public ResponseHelper<String> APIExceptionHandler(APIException e) {
//        return new ResponseHelper<>(e.getCode(), e.getMsg());
//    }
//
//    @ExceptionHandler(JWTVerificationException.class)
//    public ResponseHelper<String> jwtVerficationExceptionHandler(JWTVerificationException e){
//        log.warn("jwt error",e);
//        return new ResponseHelper<>(ResponseCode.TOKEN_ERROR,e.getMessage());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseHelper<String> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
//        return new ResponseHelper<>(ResponseCode.VALIDATE_FAILED, objectError.getDefaultMessage());
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseHelper<String> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
//        return new ResponseHelper<>(ResponseCode.VALIDATE_FAILED, e.getMessage());
//    }
//
//    @ExceptionHandler(JWTDecodeException.class)
//    public ResponseHelper<String> JWTDecodeExceptionHandler(JWTDecodeException e) {
//        return new ResponseHelper<>(ResponseCode.TOKEN_ERROR, "token错误，请重新登录");
//    }
//
//    @ExceptionHandler(SignatureVerificationException.class)
//    public ResponseHelper<String> SignatureVerificationExceptionHandler(SignatureVerificationException e) {
//        return new ResponseHelper<>(ResponseCode.TOKEN_ERROR, "token错误，请重新登录");
//    }
//
//    @ExceptionHandler(MissingServletRequestPartException.class)
//    public ResponseHelper<String> MissingServletRequestPartExceptionHandler(MissingServletRequestPartException e) {
//        return new ResponseHelper<>(ResponseCode.VALIDATE_FAILED, e.getMessage());
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public ResponseHelper<String> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
//        return new ResponseHelper<>(ResponseCode.VALIDATE_FAILED, e.getMessage());
//    }
//
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseHelper<String> SQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e) {
//        return new ResponseHelper<>(ResponseCode.DATABASE_FAILED, e.getMessage());
//    }
//
//    @ExceptionHandler(Throwable.class)
//    public ResponseHelper<String> AnyExceptionHandler(Throwable e) {
//        e.printStackTrace();
//        return new ResponseHelper<>(ResponseCode.ERROR, e.getClass().getName());
//    }
//}
