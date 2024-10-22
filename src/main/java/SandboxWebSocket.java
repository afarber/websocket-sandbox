import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class SandboxWebSocket {
    private static final Logger LOG = LoggerFactory.getLogger(SandboxWebSocket.class);

    private Session session;

    @OnWebSocketOpen
    public void onWebSocketOpen(Session session) {
        LOG.info("CONNECTED: {}", session.getRemoteSocketAddress());
        this.session = session;
    }

    @OnWebSocketClose
    public void onWebSocketClose(int statusCode, String reason) {
        this.session = null;
        LOG.info("CLOSE: {} {}", statusCode, reason);
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable cause) {
        LOG.info("ERROR: {}", cause);
    }

    @OnWebSocketMessage
    public void onWebSocketText(String message) {
        LOG.info("RECEIVED: {} characters", message.length());
        session.sendText("Text length is " + message.length(), Callback.NOOP);
        session.sendText("Text hash is " + message.hashCode(), Callback.NOOP);
    }
}
