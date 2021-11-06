package com.knchen.tftp.server;

import com.knchen.tftp.server.store.TFTPStore;
import com.knchen.tftp.server.store.TFTPTestStore;

/**
 * tftp-server测试
 *
 * @author knchen
 * @date 2021/11/4 10:37
 */
public class TFTPServerTest {
    public static void main(String[] args) {
        TFTPServer tftpServer = new TFTPServer(new TFTPTestStore(TFTPStore.TFTPStoreMode.READ_AND_WRITE));
        tftpServer.start(8989);
    }
}
