package com.knchen.tftp.server.transfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.packet.TFTPRequestPacket;
import com.knchen.tftp.server.store.TFTPStore;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * tftp 数据传输
 *
 * @param <open> read/write
 * @param <receive> 接收的tftp包类型
 * @param <send> 发送的tftp类型
 * @author knchen
 * @since 2021/10/31
 */
public abstract class AbstractTFTPTransfer<open extends TFTPRequestPacket, receive extends TFTPPacket,
    send extends TFTPPacket> implements TFTPTransfer<open, receive, send> {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractTFTPTransfer.class);

    // 传输完成标识
    protected boolean complete = false;

    // 重试次数
    protected int retryTimes;

    // 上一次发送的包，用于重试
    protected send lastPacket;

    // 通道
    private Channel channel;

    // 文件名
    protected String fileName;

    // 数据
    protected ByteBuf content;

    // 当前帧号
    protected int blockNumber;

    @Override
    public void open(Channel channel, open packet) {
        this.channel = channel;
        fileName = packet.getFileName();
        channel.attr(TFTPTransfer.TFTP_TRANSFER).set(this);
        open0(packet);
    }

    protected abstract void open0(open packet);

    @Override
    public void transfer(send packet) {
        lastPacket = packet;
        channel.writeAndFlush(packet);
    }

    @Override
    public synchronized void timeout() {
        if (complete) {
            close();
            return;
        }

        if (++retryTimes > TFTPTransfer.MAX_RETRY_TIMES) {
            close();
            throw new TFTPTransferException(
                String.format("%s timeout more than %d times, fileName=%s, current blockNumber=%d",
                    this.getClass().getSimpleName(), TFTPTransfer.MAX_RETRY_TIMES, fileName, blockNumber));
        }

        transfer(lastPacket);
    }

    @Override
    public void close() {
        LOG.debug("{} {}, fileName={}, current blockNumber={}", this.getClass().getSimpleName(), complete, fileName,
            blockNumber);
        channel.close();
    }

    @Override
    public TFTPStore store() {
        return channel.attr(TFTPStore.TFTP_DATA_STORE).get();
    }

    public synchronized int blockNumberIncr() {
        return blockNumber = blockNumber + 1 > 65535 ? 0 : blockNumber + 1;
    }
}
