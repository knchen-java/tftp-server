package com.knchen.tftp.server.packet;

/**
 * tftp 报文异常
 *
 * @author knchen
 * @date 2021/11/2 17:57
 */
public class TFTPPacketException extends RuntimeException {
    public TFTPPacketException() {}

    public TFTPPacketException(final String message) {
        super(message);
    }
}
