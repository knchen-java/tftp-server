package com.knchen.tftp.server.store;

import com.knchen.tftp.server.packet.TFTPErrorCode;
import com.knchen.tftp.server.packet.TFTPPacket;
import com.knchen.tftp.server.packet.TFTPPacketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 抽象 tftp 数据仓
 *
 * @author knchen
 * @date 2021/11/4 9:15
 */
public abstract class AbstractTFTPStore implements TFTPStore {
    protected static final Logger LOG = LoggerFactory.getLogger(TFTPStore.class);

    private TFTPStoreMode mode;

    public AbstractTFTPStore(TFTPStoreMode mode) {
        this.mode = mode;
    }

    @Override
    public TFTPErrorCode check(InetSocketAddress remote, String fileName, int type) {
        // 检查仓库的读写权限
        if (mode != TFTPStoreMode.READ_AND_WRITE && mode.getType() != type) {
            LOG.warn("illegal TFTP operation, operation={}, fileName={}", type, fileName);
            return TFTPErrorCode.ILLEGAL_OPERATION;
        }

        return check0(remote, fileName, request(type));
    }

    /**
     * 将对应数字转化成枚举，便于理解
     *
     * @param type 请求类型
     * @return 请求类型枚举
     */
    private TFTPRequest request(int type) {
        if (type == TFTPPacket.READ_REQUEST) return TFTPRequest.READ;
        if (type == TFTPPacket.WRITE_REQUEST) return TFTPRequest.WRITE;
        throw new TFTPPacketException("bad tftp packet, it's not a request packet, type=" + type);
    }

    /**
     * 校验请求合法性
     *
     * @param remote   对端地址
     * @param fileName 文件名
     * @param request  请求类型
     * @return 校验结果
     */
    public abstract TFTPErrorCode check0(InetSocketAddress remote, String fileName, TFTPRequest request);
}
