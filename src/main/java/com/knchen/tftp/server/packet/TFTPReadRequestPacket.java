package com.knchen.tftp.server.packet;

import io.netty.channel.socket.DatagramPacket;

/**
 * tftp 读请求
 *
 * @author knchen
 * @date 2021/11/2 10:46
 */
public class TFTPReadRequestPacket extends TFTPRequestPacket {

    {
        this.type = TFTPPacket.READ_REQUEST;
    }

    public TFTPReadRequestPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);
    }
}
