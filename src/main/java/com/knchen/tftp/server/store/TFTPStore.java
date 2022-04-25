package com.knchen.tftp.server.store;

import java.net.InetSocketAddress;

import com.knchen.tftp.server.packet.TFTPErrorCode;
import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.transfer.TFTPTransferMode;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;

/**
 * tftp 数据仓
 *
 * @author knchen
 * @date 2021/11/1 18:36
 */
public interface TFTPStore {
    AttributeKey<TFTPStore> TFTP_DATA_STORE = AttributeKey.newInstance("tftp_store");

    /**
     * 仓库模式
     */
    enum TFTPStoreMode {
        READ_ONLY(TFTPPacket.READ_REQUEST), WRITE_ONLY(TFTPPacket.WRITE_REQUEST), READ_AND_WRITE(-1);

        private int type;

        TFTPStoreMode(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 请求类型
     */
    enum TFTPRequest {
        READ, WRITE
    }

    /**
     * 校验请求合法性
     *
     * @param remote 对端地址
     * @param fileName 文件名
     * @param type 请求类型
     * @return 校验结果
     */
    TFTPErrorCode check(InetSocketAddress remote, String fileName, int type);

    /**
     * 接收文件
     *
     * @param fileName 文件名
     * @param data 文件内容
     * @param mode tftp传输模式
     */
    void accept(String fileName, ByteBuf data, TFTPTransferMode mode);

    /**
     * 获取文件
     *
     * @param fileName 文件名
     * @param mode tftp传输模式
     * @return 文件内容
     */
    ByteBuf get(String fileName, TFTPTransferMode mode);
}
