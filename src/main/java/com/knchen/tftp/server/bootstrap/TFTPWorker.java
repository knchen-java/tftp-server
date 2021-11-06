package com.knchen.tftp.server.bootstrap;

import com.knchen.tftp.server.codec.TFTPCodec;
import com.knchen.tftp.server.handler.*;
import com.knchen.tftp.server.packet.TFTPErrorCode;
import com.knchen.tftp.server.packet.TFTPRequestPacket;
import com.knchen.tftp.server.store.TFTPStore;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * 处理 tftp 数据传输 data/ack 指令
 *
 * @author knchen
 * @since 2021/10/31
 */
public class TFTPWorker {
    public static final AttributeKey<TFTPWorker> TFTP_WORKER = AttributeKey.newInstance("tftp_worker");

    private Bootstrap bootstrap = new Bootstrap();

    private EventLoopGroup group = new NioEventLoopGroup();

    private TFTPStore store;

    public TFTPWorker(TFTPStore store) {
        this.store = store;
        init();
    }

    private void init() {
        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.attr(TFTPStore.TFTP_DATA_STORE).set(store);
                channel.pipeline()
                        .addLast("codec", TFTPCodec.SINGLETON)
                        .addLast("timeout", new TFTPTimeoutHandler())
                        .addLast("data", TFTPDataHandler.SINGLETON)
                        .addLast("ack", TFTPAckHandler.SINGLETON)
                        .addLast("error", TFTPErrorHandler.SINGLETON)
                        .addLast("exception", TFTPExceptionHandler.SINGLETON);
            }
        });
    }

    /**
     * 开启新channel用于传输数据
     *
     * @param inetSocketAddress 地址
     * @return ChannelFuture
     */
    public ChannelFuture start(InetSocketAddress inetSocketAddress) {
        return bootstrap.connect(inetSocketAddress);
    }

    /**
     * 停止
     */
    public void stop() {
        group.shutdownGracefully();
    }

    /**
     * 校验请求合法性
     *
     * @param packet tftp 请求
     * @return 校验结果
     */
    public TFTPErrorCode check(TFTPRequestPacket packet) {
        return store.check(packet.sender(), packet.getFileName(), packet.getType());
    }
}
