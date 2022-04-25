package com.knchen.tftp.server.transfer;

import java.net.InetSocketAddress;

import com.knchen.tftp.server.packet.TFTPAckPacket;
import com.knchen.tftp.server.packet.TFTPDataPacket;
import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.packet.TFTPReadRequestPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * tftp 读传输
 *
 * @author knchen
 * @since 2021/10/31
 */
public class TFTPReadTransfer extends AbstractTFTPTransfer<TFTPReadRequestPacket, TFTPAckPacket, TFTPDataPacket> {
    @Override
    protected void open0(TFTPReadRequestPacket packet) {
        String filename = packet.getFileName();
        content = store().get(filename, packet.getMode());

        // 发送第一帧
        blockNumber = 1;
        transfer(packet.sender(), blockNumber);
    }

    @Override
    public synchronized void handle(TFTPAckPacket packet) {
        int length = lastPacket.data().readableBytes();

        // 帧号与预期不同, 忽略并等待超时重传
        if (packet.getBlockNumber() != this.blockNumber) {
            return;
        }

        // 上一帧的帧长小于512, 说明传输完成
        if (length < TFTPPacket.SEGMENT_SIZE) {
            complete = true;
            close();
            return;
        }

        // 数据刚好是512的倍数时，补一帧空帧作为结束
        // 正常传输分支
        transfer(packet.sender(), blockNumberIncr());
    }

    private void transfer(InetSocketAddress recipient, int blockNumber) {
        // ByteBuf::readBytes(int var1)返回池化buf，但是该包有可能被多次使用，避免buf池泄露，使用Unpooled
        int length = Math.min(TFTPPacket.SEGMENT_SIZE, content.readableBytes());
        ByteBuf buffer = Unpooled.buffer(length);
        content.readBytes(buffer);
        TFTPDataPacket data = new TFTPDataPacket(recipient, buffer, blockNumber);
        transfer(data);
    }
}
