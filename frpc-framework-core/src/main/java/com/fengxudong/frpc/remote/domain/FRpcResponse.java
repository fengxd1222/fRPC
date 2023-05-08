package com.fengxudong.frpc.remote.domain;

import com.fengxudong.frpc.enums.FRpcResponseEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author feng xud
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FRpcResponse<T> implements Serializable {
    /**
     * requestId
     */
    private String requestId;
    /**
     * res code
     */
    private Integer code;
    /**
     * res message
     */
    private String message;
    /**
     * res body
     */
    private T data;


    public static <T> FRpcResponse<T> ok(String requestId,T data){
        return FRpcResponse.<T>builder()
                .code(FRpcResponseEnum.OK.getCode())
                .message(FRpcResponseEnum.OK.getMessage())
                .data(data)
                .requestId(requestId)
                .build();
    }

    public static <T> FRpcResponse<T> error(Exception exception,String requestId){
        return error(exception.getMessage(),FRpcResponseEnum.ERROR,requestId);
    }

    public static <T> FRpcResponse<T> error(String message,FRpcResponseEnum fRpcResponseEnum,String requestId){
        return FRpcResponse.<T>builder()
                .code(fRpcResponseEnum.getCode())
                .message(message)
                .requestId(requestId)
                .build();
    }
}
