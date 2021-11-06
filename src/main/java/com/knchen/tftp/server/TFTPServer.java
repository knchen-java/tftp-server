package com.knchen.tftp.server;

import com.knchen.tftp.server.bootstrap.TFTPBoos;
import com.knchen.tftp.server.store.TFTPStore;

/**
 * 使用 netty 实现 tftp-server 参考 apache-common-net
 *
 * @author knchen
 * @since 2021/10/30
 */
public class TFTPServer {
    private TFTPBoos boos;

    // 0=初始化，1=运行中，-1=销毁
    private int status = 0;

    public TFTPServer(TFTPStore store) {
        this.boos = new TFTPBoos(store);
    }

    public void start() {
        start(69);
    }

    /**
     * 启动 tftp-server
     * 只能启动一次
     *
     * @param port port
     */
    public synchronized void start(int port) {
        if (status != 0) {
            throw new IllegalStateException();
        }
        status = 1;
        boos.start(port);
    }

    /**
     * 销毁 tftp-server
     * 销毁后无法再次启动和销毁
     */
    public synchronized void destroy() {
        if (status == -1) {
            throw new IllegalStateException();
        }
        status = -1;
        boos.stop();
    }
}
