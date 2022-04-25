package com.knchen.tftp.server.handler;

import com.knchen.tftp.server.packet.TFTPDataPacket;
import com.knchen.tftp.server.transfer.TFTPTransfer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * tftp 数据包处理
 *
 * @author knchen
 * @since 2021/10/30
 */
@ChannelHandler.Sharable
public class TFTPDataHandler extends SimpleChannelInboundHandler<TFTPDataPacket> {
    public static final TFTPDataHandler SINGLETON = new TFTPDataHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TFTPDataPacket tftpDataPacket)
        throws Exception {
        TFTPTransfer tftpTransfer = channelHandlerContext.channel().attr(TFTPTransfer.TFTP_TRANSFER).get();
        tftpTransfer.handle(tftpDataPacket);
    }
}
