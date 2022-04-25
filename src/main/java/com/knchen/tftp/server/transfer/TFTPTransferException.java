package com.knchen.tftp.server.transfer;

/**
 * tftp 传输异常
 *
 * @author knchen
 * @date 2021/11/2 18:30
 */
public class TFTPTransferException extends RuntimeException {
    public TFTPTransferException() {}

    public TFTPTransferException(String message) {
        super(message);
    }
}
