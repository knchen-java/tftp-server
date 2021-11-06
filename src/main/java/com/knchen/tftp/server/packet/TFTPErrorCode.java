package com.knchen.tftp.server.packet;

/**
 * tftp 错误码
 * NORMAL 为系统使用，不在协议中体现
 *
 * @author knchen
 * @date 2021/11/3 10:15
 */
public enum TFTPErrorCode {
    /**
     * 正常，服务器自己使用
     */
    NORMAL(-1, "normal."),

    /**
     * 未知错误
     */
    UNDEFINED(0, "not defined."),

    /**
     * 文件找不到
     */
    FILE_NOT_FOUND(1, "file not found."),

    /**
     * 拒绝访问
     */
    ACCESS_VIOLATION(2, "access violation."),

    /**
     * 磁盘满了或者超出可分配空间
     */
    OUT_OF_SPACE(3, "out of space."),

    /**
     * 非法操作
     */
    ILLEGAL_OPERATION(4, "illegal TFTP operation."),

    /**
     * 未知的传输id
     */
    UNKNOWN_TID(5, "unknown transfer ID."),

    /**
     * 文件已存在
     */
    FILE_EXISTS(6, "file already exists."),

    /**
     * 没有该用户
     */
    NO_SUCH_USER(7, "no such user.");

    private int code;

    private String message;

    TFTPErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }

    public static TFTPErrorCode valueOfCode(int code) {
        for (TFTPErrorCode value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new TFTPPacketException("unknown TFTP error code, code=" + code);
    }
}
