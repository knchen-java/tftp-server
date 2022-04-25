package com.knchen.tftp.server.handler;

import com.knchen.tftp.server.bootstrap.TFTPWorker;
import com.knchen.tftp.server.packet.TFTPWriteRequestPacket;
import com.knchen.tftp.server.transfer.TFTPWriteTransfer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * tftp写请求处理
 *
 * @author knchen
 * @since 2021/10/30
 */
@ChannelHandler.Sharable
public class TFTPWriteRequestHandler extends AbstractTFTPRequestHandler<TFTPWriteRequestPacket> {
    public static final TFTPWriteRequestHandler SINGLETON = new TFTPWriteRequestHandler();

    @Override
    protected void channelRead0(TFTPWorker worker, TFTPWriteRequestPacket packet) throws Exception {
        Channel channel = worker.start(packet.sender()).sync().channel();
        new TFTPWriteTransfer().open(channel, packet);
    }
}
