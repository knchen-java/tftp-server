package com.knchen.tftp.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tftp 统一异常处理
 *
 * @author knchen
 * @since 2021/11/6
 */
@ChannelHandler.Sharable
public class TFTPExceptionHandler extends ChannelInboundHandlerAdapter {
    public static final TFTPExceptionHandler SINGLETON = new TFTPExceptionHandler();

    private static final Logger LOG = LoggerFactory.getLogger(TFTPExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("tftp-server caught an inbound exception", cause);
    }
}
