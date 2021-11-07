package com.knchen.tftp.server;

import com.knchen.tftp.server.packet.TFTPErrorCode;
import com.knchen.tftp.server.store.AbstractTFTPStore;
import com.knchen.tftp.server.transfer.TFTPTransferMode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashSet;

/**
 * tftp 测试仓库
 *
 * @author knchen
 * @since 2021/11/2
 */
public class TFTPTestStore extends AbstractTFTPStore {
    public TFTPTestStore(TFTPStoreMode mode) {
        super(mode);
    }

    private HashSet<String> whitelist = new HashSet<>();

    {
        whitelist.add("127.0.0.1");
    }

    @Override
    public TFTPErrorCode check0(InetSocketAddress remote, String fileName, TFTPRequest request) {
        // 校验对端身份
        if (!whitelist.contains(remote.getHostName())) return TFTPErrorCode.ACCESS_VIOLATION;

        // 根据请求类型进行文件校验
        switch (request) {
            case READ:
                return new File(fileName).exists() ? TFTPErrorCode.NORMAL : TFTPErrorCode.FILE_NOT_FOUND;
            case WRITE:
                return new File(fileName).exists() ? TFTPErrorCode.FILE_EXISTS : TFTPErrorCode.NORMAL;
            default:
                return TFTPErrorCode.NORMAL;
        }
    }

    @Override
    public void accept(String fileName, ByteBuf data, TFTPTransferMode mode) {
        try (OutputStream out = new FileOutputStream(fileName)) {
            out.write(ByteBufUtil.getBytes(data));
        } catch (Exception e) {
            LOG.error("tftp-server fail to write file, fileName={}", fileName, e);
        }
    }

    @Override
    public ByteBuf get(String fileName, TFTPTransferMode mode) {
        try (InputStream in = new FileInputStream(fileName)) {
            int length = in.available();
            ByteBuf data = Unpooled.buffer(length);
            data.writeBytes(in, length);
            return data;
        } catch (Exception e) {
            LOG.error("tftp-server fail to read file, fileName={}", fileName, e);
        }
        return Unpooled.EMPTY_BUFFER;
    }
}
