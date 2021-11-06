package com.knchen.tftp.server.handler;

import com.knchen.tftp.server.bootstrap.TFTPWorker;
import com.knchen.tftp.server.packet.TFTPReadRequestPacket;
import com.knchen.tftp.server.transfer.TFTPReadTransfer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;

/**
 * tftp读请求处理
 *
 * @author knchen
 * @since 2021/10/30
 */
@ChannelHandler.Sharable
public class TFTPReadRequestHandler extends AbstractTFTPRequestHandler<TFTPReadRequestPacket> {
    public static final TFTPReadRequestHandler SINGLETON = new TFTPReadRequestHandler();

    @Override
    protected void channelRead0(TFTPWorker worker, TFTPReadRequestPacket packet) throws Exception {
        Channel channel = worker.start(packet.sender()).sync().channel();
        new TFTPReadTransfer().open(channel, packet);
    }
}
