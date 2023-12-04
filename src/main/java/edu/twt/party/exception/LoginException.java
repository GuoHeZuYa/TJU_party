package edu.twt.party.exception;

import lombok.Getter;

/**
 * @author Guohezu
 */
@Getter
public class LoginException extends RuntimeException{
    private String info;
    public LoginException(String info){
        super(info);
    }
}
