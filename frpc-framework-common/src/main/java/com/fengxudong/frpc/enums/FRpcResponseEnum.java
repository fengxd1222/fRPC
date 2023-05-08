package com.fengxudong.frpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author feng xud
 */
@Getter
@AllArgsConstructor
public enum FRpcResponseEnum {
    OK(200, "ok"),
    ERROR(500, "error");
    private final int code;

    private final String message;
}
