package com.ibdknox.socket_io_netty;

public interface INSIOHandler {
    void OnConnect(INSIOClient ws);
    void OnMessage(INSIOClient ws, String message);
    void OnDisconnect(INSIOClient ws);
    void OnShutdown();
}
