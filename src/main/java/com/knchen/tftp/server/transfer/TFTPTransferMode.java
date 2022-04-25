package com.knchen.tftp.server.transfer;

import java.util.Objects;

/**
 * tftp 传输模式
 *
 * @author knchen
 * @date 2021/11/2 18:04
 */
public enum TFTPTransferMode {
    NETASCII("netascii"),

    OCTET("octet");

    private String name;

    TFTPTransferMode(String name) {
        this.name = name;
    }

    public static TFTPTransferMode valueOfName(String name) {
        name = name.toLowerCase(java.util.Locale.ENGLISH);
        for (TFTPTransferMode mode : values()) {
            if (Objects.equals(mode.name, name)) {
                return mode;
            }
        }
        throw new TFTPTransferException("Unknown TFTP transfer mode, name=" + name);
    }
}
