package com.knchen.tftp.server.bootstrap;

import com.knchen.tftp.server.codec.TFTPCodec;
import com.knchen.tftp.server.handler.TFTPExceptionHandler;
import com.knchen.tftp.server.handler.TFTPReadRequestHandler;
import com.knchen.tftp.server.handler.TFTPWriteRequestHandler;
import com.knchen.tftp.server.store.TFTPStore;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * 监听 tftp 读写请求 read/write 指令
 *
 * @author knchen
 * @since 2021/10/31
 */
public class TFTPBoos {
    private Bootstrap bootstrap = new Bootstrap();

    private EventLoopGroup group = new NioEventLoopGroup(1);

    private TFTPWorker worker;

    public TFTPBoos(TFTPStore store) {
        this.worker = new TFTPWorker(store);
        init();
    }

    private void init() {
        bootstrap.group(group).channel(NioDatagramChannel.class).handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.attr(TFTPWorker.TFTP_WORKER).set(worker);
                channel.pipeline().addLast("codec", TFTPCodec.SINGLETON)
                    .addLast("read", TFTPReadRequestHandler.SINGLETON)
                    .addLast("write", TFTPWriteRequestHandler.SINGLETON)
                    .addLast("exception", TFTPExceptionHandler.SINGLETON);
            }
        });
    }

    /**
     * 启动 tftp-server 监听端口
     *
     * @param port 端口
     */
    public void start(int port) {
        bootstrap.bind(port);
    }

    /**
     * 停止 tftp-server
     */
    public void stop() {
        group.shutdownGracefully();
        worker.stop();
    }
}
