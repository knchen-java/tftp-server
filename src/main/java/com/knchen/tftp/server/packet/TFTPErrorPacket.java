package com.knchen.tftp.server.packet;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

/**
 * tftp 错误包
 *
 * @author knchen
 * @date 2021/11/2 10:48
 */
public class TFTPErrorPacket extends TFTPPacket {
    private TFTPErrorCode code;

    private String message;

    {
        this.type = TFTPPacket.ERROR;
    }

    public TFTPErrorPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);
        ByteBuf content = datagramPacket.content();
        int code = (((content.getByte(2) & 0xff) << 8) | (content.getByte(3) & 0xff));
        this.code = TFTPErrorCode.valueOfCode(code);
        content.skipBytes(4);
        if (content.readableBytes() > 1) {
            byte[] bytes = ByteBufUtil.getBytes(content, 4, content.readableBytes() - 1);
            this.message = new String(bytes);
        }

    }

    public TFTPErrorPacket(InetSocketAddress recipient, TFTPErrorCode code, String message) {
        super(recipient);
        this.code = code;
        this.message = message;
    }

    public TFTPErrorPacket(InetSocketAddress recipient, TFTPErrorCode code) {
        super(recipient);
        this.code = code;
        this.message = code.getMessage();
    }

    @Override
    public DatagramPacket encode() {
        byte[] msg = message.getBytes();
        ByteBuf data = Unpooled.buffer(5 + msg.length).writeByte(0).writeByte(type)
            .writeByte((byte)((code.getCode() & 0xffff) >> 8)).writeByte((byte)(code.getCode() & 0xff)).writeBytes(msg)
            .writeByte(0);
        return new DatagramPacket(data, recipient);
    }

    @Override
    public String toString() {
        return String.format("TFTP error packet, %s=>%s, code=%d, message=%s", sender, recipient, code.getCode(),
            message);
    }
}
