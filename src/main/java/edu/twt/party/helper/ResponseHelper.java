package edu.twt.party.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ResponseHelper<T> {
    private int code;
    private String msg;
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Page page;

    public ResponseHelper(){
        this(ResponseCode.SUCCESS, (T) Boolean.valueOf(true));
    }

    public ResponseHelper(T t) {
        if(t == null){
            this.code =  ResponseCode.TARGET_NOT_EXIST.getCode();
            this.msg = ResponseCode.TARGET_NOT_EXIST.getMsg();
        }else{
            this.code = ResponseCode.SUCCESS.getCode();
            this.msg = ResponseCode.SUCCESS.getMsg();
        }
        this.data = t;
    }

    public ResponseHelper(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = data;
    }
    public ResponseHelper(Page page,T data){
        this(data);
        this.page = page;
    }

    public ResponseHelper(ResponseCode responseCode, Boolean data,String o) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.data = (T) data;
    }



    public  static <T> ResponseHelper<T> error(ResponseCode responseCode){
        return new ResponseHelper<>(responseCode,false,"");
    }

    public  static <T> ResponseHelper<T> error(){
        return ResponseHelper.error(ResponseCode.ERROR);
    }


}
