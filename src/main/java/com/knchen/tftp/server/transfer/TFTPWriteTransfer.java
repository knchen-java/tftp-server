package com.knchen.tftp.server.transfer;

import com.knchen.tftp.server.packet.TFTPAckPacket;
import com.knchen.tftp.server.packet.TFTPDataPacket;
import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.packet.TFTPWriteRequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetSocketAddress;

/**
 * tftp 写传输
 *
 * @author knchen
 * @date 2021/11/2 13:44
 */
public class TFTPWriteTransfer extends AbstractTFTPTransfer<TFTPWriteRequestPacket, TFTPDataPacket, TFTPAckPacket> {
    private TFTPTransferMode mode;

    @Override
    protected void open0(TFTPWriteRequestPacket packet) {
        mode = packet.getMode();
        content = Unpooled.buffer();
        transfer(packet.sender(), 0);
        blockNumberIncr();
    }

    @Override
    public synchronized void handle(TFTPDataPacket packet) {
        ByteBuf data = packet.data();

        // 帧号与预期不同，忽略
        if (packet.getBlockNumber() != blockNumber) {
            return;
        }

        transfer(packet.sender(), blockNumber);
        blockNumberIncr();
        int length = data.readableBytes();
        content.writeBytes(data);

        // 数据长度小于512，说明传输完成
        if (!complete && length < TFTPPacket.SEGMENT_SIZE) {
            // 最后一帧有可能丢失，继续监听，再次发生超时时，说明对端没有重传，已接收到最后一个响应，这个时候再关闭channel
            complete = true;

            store().accept(fileName, content, mode);
        }
    }

    private void transfer(InetSocketAddress recipient, int blockNumber) {
        TFTPAckPacket data = new TFTPAckPacket(recipient, blockNumber);
        transfer(data);
    }
}
