package com.knchen.tftp.server.codec;

import com.knchen.tftp.server.packet.TFTPPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * tftp编解码器
 *
 * @author knchen
 * @since 2021/10/30
 */
@ChannelHandler.Sharable
public class TFTPCodec extends MessageToMessageCodec<DatagramPacket, TFTPPacket> {
    public static final TFTPCodec SINGLETON = new TFTPCodec();

    private static final Logger LOG = LoggerFactory.getLogger(TFTPCodec.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TFTPPacket tftpPacket, List<Object> list) throws Exception {
        try {
            DatagramPacket datagramPacket = tftpPacket.encode();
            list.add(datagramPacket);
        } catch (Exception e) {
            LOG.error("tftp-server caught an outbound exception", e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        TFTPPacket tftpPacket = TFTPPacket.decode(datagramPacket);
        list.add(tftpPacket);
    }
}
