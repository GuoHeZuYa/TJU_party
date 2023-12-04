package edu.twt.party.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(0,"成功"),


    FAILED(40001,"失败"),
    VALIDATE_FAILED(40002, "参数校验失败"),
    DATABASE_FAILED(40003, "数据库错误"),
    SERVER_FAILED(40004, "服务器错误"),
    LOGIN_FAILED(40005,"登录失败"),
    TARGET_NOT_EXIST(40006,"目标不存在"),
    WRONG_SNO_FOR_NAME(40007,"错误的姓名和学号"),
    WRONG_TARGET_BRANCH(40008,"导入失败,请确认学生属于目标党支部学院,转专业学生请先更新学院"),



    ERROR(50000, "未知错误"),


    TOKEN_EMPTY(40300,"token为空"),
    TOKEN_ERROR(40301,"token错误"),
    TOKEN_EXPIRED(40302,"token失效"),
    PLEASE_LOGIN(40303,"please login"),
    NO_PERMISSION(40304,"no permission"),
    PARTY_BRANCH_NOT_EXITST(30003,"党支部不存在"),

    FILE_FORMAT_WRONG(30005,"文件格式错误"),


    SOURCE_NOT_EXIST(50001,"the source to access do not exist");




    private int code;
    private String msg;
}
