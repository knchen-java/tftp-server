package com.knchen.tftp.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knchen.tftp.server.packet.TFTPErrorPacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * tftp 错误包处理
 *
 * @author knchen
 * @date 2021/11/2 11:13
 */
@ChannelHandler.Sharable
public class TFTPErrorHandler extends SimpleChannelInboundHandler<TFTPErrorPacket> {
    public static final TFTPErrorHandler SINGLETON = new TFTPErrorHandler();

    private static final Logger LOG = LoggerFactory.getLogger(TFTPErrorHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TFTPErrorPacket tftpErrorPacket)
        throws Exception {
        LOG.warn(tftpErrorPacket.toString());
        channelHandlerContext.close();
    }
}
