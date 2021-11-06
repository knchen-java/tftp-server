package com.knchen.tftp.server.packet;

import io.netty.channel.socket.DatagramPacket;

/**
 * tftp 写请求
 *
 * @author knchen
 * @date 2021/11/2 10:47
 */
public class TFTPWriteRequestPacket extends TFTPRequestPacket {

    {
        this.type = TFTPPacket.WRITE_REQUEST;
    }

    public TFTPWriteRequestPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);
    }
}
