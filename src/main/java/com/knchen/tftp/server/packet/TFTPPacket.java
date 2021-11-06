package com.knchen.tftp.server.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

/**
 * tftp 报文
 *
 * @author knchen
 * @date 2021/11/2 8:54
 */
public abstract class TFTPPacket {
    // 最小报文长度
    public static final int MIN_PACKET_SIZE = 4;

    // 报文长度
    public static final int SEGMENT_SIZE = 512;

    // 读请求
    public static final int READ_REQUEST = 1;

    // 写请求
    public static final int WRITE_REQUEST = 2;

    // 数据
    public static final int DATA = 3;

    // 响应
    public static final int ACKNOWLEDGEMENT = 4;

    // 错误
    public static final int ERROR = 5;

    // 报文类型
    protected int type;

    protected InetSocketAddress sender;

    protected InetSocketAddress recipient;

    protected TFTPPacket(InetSocketAddress recipient) {
        this.recipient = recipient;
    }

    public TFTPPacket(DatagramPacket datagramPacket) {
        this.sender = datagramPacket.sender();
        this.recipient = datagramPacket.recipient();
    }

    public static TFTPPacket decode(DatagramPacket datagramPacket) {
        ByteBuf content = datagramPacket.content();
        if (content.readableBytes() < MIN_PACKET_SIZE) {
            throw new TFTPPacketException("bad tftp packet, datagram data length is too short");
        }
        TFTPPacket packet = null;
        switch (content.getByte(1)) {
            case READ_REQUEST:
                packet = new TFTPReadRequestPacket(datagramPacket);
                break;
            case WRITE_REQUEST:
                packet = new TFTPWriteRequestPacket(datagramPacket);
                break;
            case DATA:
                packet = new TFTPDataPacket(datagramPacket);
                break;
            case ACKNOWLEDGEMENT:
                packet = new TFTPAckPacket(datagramPacket);
                break;
            case ERROR:
                packet = new TFTPErrorPacket(datagramPacket);
                break;
            default:
                throw new TFTPPacketException("bad tftp packet, invalid TFTP operator code");
        }
        return packet;
    }

    /**
     * tftp编码器
     *
     * @return DatagramPacket
     */
    public abstract DatagramPacket encode();

    /**
     * 发送端
     *
     * @return 地址
     */
    public InetSocketAddress sender() {
        return this.sender;
    }

    /**
     * 接收端
     *
     * @return 地址
     */
    public InetSocketAddress recipient() {
        return this.recipient;
    }
}
