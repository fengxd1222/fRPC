package com.fengxudong.frpc.constant;

/**
 * @author feng xud
 */
public class FRpcConstant {
    public static final String FRPC_SCAN_ATTRIBUTE_NAME = "basePackages";

    /**
     *     =====================    加解码相关 codec         ==============
     */
    public static final byte[] MAGIC_NUMBER = {0x12, 0x34, 0x56, 0x78};
    public static final byte[] VERSION = {0x01, 0x00, 0x00, 0x00};
    public static final int LENGTH_FIELD_OFFSET = 0;
    public static final int LENGTH_FIELD_LENGTH = 4;
    public static final int MAGIC_NUMBER_LENGTH = 4;
    public static final int VERSION_LENGTH = 4;


    /**
     *    =====================   message type ===========================
     */

    public static class MessageType{
        public static final byte REQUEST_TYPE = 1;

        public static final byte RESPONSE_TYPE = 2;

        public static final byte HEARTBEAT_REQUEST_TYPE = 3;

        public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    }


    public static class ZkConfig{
        public static final String ZK_CONFIG_FILE_NAME = "frpc.properties";
        public static final String ZK_CONFIG_CLASS_PATH = "classpath*:frpc.properties";
        public static final String ZK_ADDRESS = "frpc.zookeeper.address";
        public static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

        public static final String ZK_ROOT_PATH = "/frpc";
    }

}
