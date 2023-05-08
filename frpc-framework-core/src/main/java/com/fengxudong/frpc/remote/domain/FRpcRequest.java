package com.fengxudong.frpc.remote.domain;

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
public class FRpcRequest implements Serializable {
    /**
     * request id
     */
    private String requestId;
    /**
     * target class name
     */
    private String classOrInterfaceName;
    /**
     * method name
     */
    private String methodName;
    /**
     * parameters
     */
    private Object[] parameters;
    /**
     * paramTypes
     */
    private Class<?>[] paramTypes;
    /**
     * service version
     */
    private String version;
    /**
     * service group
     */
    private String group;

}
