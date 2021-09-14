package com.purejoy.musicshareroom.common.dto;

import com.purejoy.musicshareroom.common.enums.CustomizeStatusEnum;
import lombok.Data;

@Data
public class ResultDto<T> {

    private Integer code;
    private String msg;
    private T obj;

    public ResultDto(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultDto(CustomizeStatusEnum statusEnum){
        this.code = statusEnum.getCode();
        this.msg = statusEnum.getMsg();
    }

    public ResultDto(){

    }

    public static ResultDto successOf(){
        ResultDto resultDto = new ResultDto(CustomizeStatusEnum.SUCCESS_CODE.getCode(),CustomizeStatusEnum.SUCCESS_CODE.getMsg());
        return resultDto;
    }

    public static <T> ResultDto successOf(T t) {
        ResultDto resultDto = new ResultDto(CustomizeStatusEnum.SUCCESS_CODE.getCode(),CustomizeStatusEnum.SUCCESS_CODE.getMsg());
        resultDto.setObj(t);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeStatusEnum statusEnum){
        ResultDto resultDto = new ResultDto(statusEnum.getCode(),statusEnum.getMsg());
        return resultDto;
    }
}
