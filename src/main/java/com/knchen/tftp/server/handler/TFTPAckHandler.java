package com.knchen.tftp.server.handler;

import com.knchen.tftp.server.packet.TFTPAckPacket;
import com.knchen.tftp.server.transfer.TFTPTransfer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * tftp响应包处理
 *
 * @author knchen
 * @since 2021/10/30
 */
@ChannelHandler.Sharable
public class TFTPAckHandler extends SimpleChannelInboundHandler<TFTPAckPacket> {
    public static final TFTPAckHandler SINGLETON = new TFTPAckHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TFTPAckPacket tftpAckPacket)
        throws Exception {
        TFTPTransfer tftpTransfer = channelHandlerContext.channel().attr(TFTPTransfer.TFTP_TRANSFER).get();
        tftpTransfer.handle(tftpAckPacket);
    }
}
