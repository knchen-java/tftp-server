package com.knchen.tftp.server.packet;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

/**
 * tftp 数据包
 *
 * @author knchen
 * @date 2021/11/2 10:47
 */
public class TFTPDataPacket extends TFTPPacket {
    private int blockNumber;

    private ByteBuf data;

    {
        this.type = TFTPPacket.DATA;
    }

    public TFTPDataPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);
        ByteBuf content = datagramPacket.content();
        this.blockNumber = (((content.getByte(2) & 0xff) << 8) | (content.getByte(3) & 0xff));
        content.skipBytes(4);
        this.data = Unpooled.wrappedBuffer(ByteBufUtil.getBytes(content));
    }

    public TFTPDataPacket(InetSocketAddress recipient, ByteBuf data, int blockNumber) {
        super(recipient);
        this.data = data;
        this.blockNumber = blockNumber;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public ByteBuf data() {
        return this.data;
    }

    @Override
    public DatagramPacket encode() {
        ByteBuf header = Unpooled.buffer(4).writeByte(0).writeByte(type).writeByte((byte)((blockNumber & 0xffff) >> 8))
            .writeByte((byte)(blockNumber & 0xff));
        ByteBuf body = Unpooled.wrappedBuffer(header, data.retain());
        return new DatagramPacket(body, recipient);
    }
}
