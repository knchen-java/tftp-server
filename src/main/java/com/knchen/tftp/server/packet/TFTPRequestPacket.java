package com.knchen.tftp.server.packet;

import java.nio.charset.Charset;

import com.knchen.tftp.server.transfer.TFTPTransferMode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;

/**
 * tftp 请求
 *
 * @author knchen
 * @date 2021/11/2 11:09
 */
public abstract class TFTPRequestPacket extends TFTPPacket {
    private String fileName;

    private TFTPTransferMode mode;

    public TFTPRequestPacket(DatagramPacket datagramPacket) {
        super(datagramPacket);

        ByteBuf content = datagramPacket.content();
        byte[] bytes = new byte[content.readableBytes()];
        content.getBytes(0, bytes);

        // 解析文件名
        int index = 2;
        ByteBuf buf = Unpooled.buffer();
        buf.markWriterIndex();
        while (index < bytes.length && bytes[index] != 0) {
            buf.writeByte(bytes[index]);
            index++;
        }
        // 文件名不能带中文
        this.fileName = buf.toString(Charset.defaultCharset());

        if (index >= bytes.length) {
            throw new TFTPPacketException("Bad file name and mode format");
        }

        // 解析传输模式
        buf.resetWriterIndex();
        index++;
        while (index < bytes.length && bytes[index] != 0) {
            buf.writeByte(bytes[index]);
            index++;
        }
        this.mode = TFTPTransferMode.valueOfName(buf.toString(Charset.defaultCharset()));
    }

    public int getType() {
        return this.type;
    }

    public String getFileName() {
        return this.fileName;
    }

    public TFTPTransferMode getMode() {
        return this.mode;
    }

    @Override
    public DatagramPacket encode() {
        // todo 这是客户端的功能，后续提取出去再完善
        throw new TFTPPacketException();
    }
}
