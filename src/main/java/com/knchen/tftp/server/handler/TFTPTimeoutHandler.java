package com.knchen.tftp.server.handler;

import java.util.concurrent.TimeUnit;

import com.knchen.tftp.server.transfer.TFTPTransfer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * tftp 超时控制 IdleStateHandler 不能共享
 *
 * @author knchen
 * @date 2021/11/3 16:45
 */
public class TFTPTimeoutHandler extends IdleStateHandler {
    public TFTPTimeoutHandler() {
        super(3, 3, 3, TimeUnit.SECONDS);
    }

    public TFTPTimeoutHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    protected final void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        // 避免客户端不断发送无效包，导致读空闲不触发，这里所有类型的空闲都会触发超时
        TFTPTransfer tftpTransfer = ctx.channel().attr(TFTPTransfer.TFTP_TRANSFER).get();
        tftpTransfer.timeout();
    }
}
