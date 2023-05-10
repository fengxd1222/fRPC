package com.fengxudong.frpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author feng xud
 */
@AllArgsConstructor
@Getter
public enum FRpcErrorEnum {
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_NOT_FOUND("没有找到指定的服务"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和响应不匹配");

    private final String message;
}
