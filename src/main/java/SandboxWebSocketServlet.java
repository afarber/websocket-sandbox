import java.time.Duration;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.ee10.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.ee10.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketCreator;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServletFactory;

public class SandboxWebSocketServlet extends JettyWebSocketServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SandboxWebSocketServlet.class);

    @Override
    public void configure(JettyWebSocketServletFactory factory) {
        factory.setCreator(new JettyWebSocketCreator() {

            @Override
            public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp)
                    throws Exception {
                resp.setExtensions(req.getExtensions().stream()
                        .filter(extension -> !extension.getName().toLowerCase().contains("permessage-deflate"))
                        .collect(Collectors.toList()));

                String userAgent = req.getHeader("User-Agent");
                LOG.info("User-Agent: {} {} {}", userAgent.contains("Mac OS X"), req.getExtensions(),
                        resp.getExtensions());

                return new SandboxWebSocket();
            }

        });
        factory.setIdleTimeout(Duration.ofMinutes(5));
    }
}