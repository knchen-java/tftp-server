package com.knchen.tftp.server.transfer;

import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.packet.TFTPRequestPacket;
import com.knchen.tftp.server.store.TFTPStore;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * tftp 数据传输
 *
 * @param <open> read/write
 * @param <receive> 接收的tftp包类型
 * @param <send> 发送的tftp类型
 * @author knchen
 * @since 2021/10/31
 */
public interface TFTPTransfer<open extends TFTPRequestPacket, receive extends TFTPPacket, send extends TFTPPacket> {
    AttributeKey<TFTPTransfer> TFTP_TRANSFER = AttributeKey.newInstance("tftp-transfer");

    // 最大重试次数
    int MAX_RETRY_TIMES = 3;

    /**
     * 开启传输通道
     *
     * @param channel 通道
     * @param packet 请求包，read/write
     */
    void open(Channel channel, open packet);

    /**
     * 处理接收到的报文，data/ack/error
     *
     * @param packet tftp 报文
     */
    void handle(receive packet);

    /**
     * 数据传输
     *
     * @param packet 帧
     */
    void transfer(send packet);

    /**
     * 超时
     */
    void timeout();

    /**
     * 数据传输完成，关闭临时通道
     */
    void close();

    /**
     * 获取数据仓库
     *
     * @return 数据仓库
     */
    TFTPStore store();
}
