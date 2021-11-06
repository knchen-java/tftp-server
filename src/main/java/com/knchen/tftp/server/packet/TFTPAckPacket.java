package com.knchen.tftp.server.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

/**
 * tftp 响应包
 *
 * @author knchen
 * @date 2021/11/2 10:48
 */
public class TFTPAckPacket extends TFTPPacket {
    private int blockNumber;

    {
        this.type = TFTPPacket.ACKNOWLEDGEMENT;
    }

    public TFTPAckPacket(InetSocketAddress recipient, int blockNumber) {
        super(recipient);
        this.blockNumber = blockNumber;
    }

    protected TFTPAckPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);
        ByteBuf content = datagramPacket.content();
        this.blockNumber = (((content.getByte(2) & 0xff) << 8) | (content.getByte(3) & 0xff));
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    @Override
    public DatagramPacket encode() {
        ByteBuf data = Unpooled.buffer(4)
                .writeByte(0)
                .writeByte(type)
                .writeByte((byte) ((blockNumber & 0xffff) >> 8))
                .writeByte((byte) (blockNumber & 0xff));
        return new DatagramPacket(data, recipient);
    }
}
