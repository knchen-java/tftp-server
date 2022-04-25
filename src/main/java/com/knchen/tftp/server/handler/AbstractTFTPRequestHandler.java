package com.knchen.tftp.server.handler;

import com.knchen.tftp.server.bootstrap.TFTPWorker;
import com.knchen.tftp.server.packet.TFTPErrorCode;
import com.knchen.tftp.server.packet.TFTPErrorPacket;
import com.knchen.tftp.server.packet.TFTPRequestPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * tftp 抽象请求处理
 *
 * @author knchen
 * @since 2021/11/3
 */
public abstract class AbstractTFTPRequestHandler<T extends TFTPRequestPacket> extends SimpleChannelInboundHandler<T> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, T packet) throws Exception {
        TFTPWorker worker = channelHandlerContext.channel().attr(TFTPWorker.TFTP_WORKER).get();

        // 校验请求/文件合法性
        TFTPErrorCode check = worker.check(packet);
        if (check != TFTPErrorCode.NORMAL) {
            TFTPErrorPacket tftpErrorPacket = new TFTPErrorPacket(packet.sender(), check);
            channelHandlerContext.writeAndFlush(tftpErrorPacket);
            return;
        }

        channelRead0(worker, packet);
    }

    /**
     * 具体处理读写事件
     *
     * @param worker tftp worker
     * @param packet tftp request packet
     * @throws Exception
     */
    protected abstract void channelRead0(TFTPWorker worker, T packet) throws Exception;
}
