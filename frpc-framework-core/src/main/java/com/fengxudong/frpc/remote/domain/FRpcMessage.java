package com.fengxudong.frpc.remote.domain;

import lombok.*;

/**
 * @author feng xud
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FRpcMessage {
    /**
     *
     */
    private byte messageType;

    private byte serializationType;

    private Object data;
}
