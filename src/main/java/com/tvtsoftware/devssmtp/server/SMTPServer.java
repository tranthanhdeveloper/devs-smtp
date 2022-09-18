
package com.tvtsoftware.devssmtp.server;

import com.tvtsoftware.devssmtp.config.SmtpServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.metrics.api.MetricFactory;
import org.apache.james.metrics.api.NoopGaugeRegistry;
import org.apache.james.metrics.api.NoopMetricFactory;
import org.apache.james.protocols.api.Protocol;
import org.apache.james.protocols.api.handler.ProtocolHandler;
import org.apache.james.protocols.netty.NettyServer;
import org.apache.james.protocols.smtp.SMTPConfigurationImpl;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.apache.james.smtpserver.netty.SmtpMetrics;
import org.apache.james.smtpserver.netty.SmtpMetricsImpl;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Collection;

@Component
@Slf4j
public class SMTPServer {
    private final SmtpServerProperties properties;

    private final Collection<ProtocolHandler> handlers;

    private NettyServer server;

    public SMTPServer(@NonNull SmtpServerProperties properties, Collection<ProtocolHandler> handlers) {
        this.properties = properties;
        this.handlers = handlers;
    }

    public void start() throws Exception {
        SMTPConfigurationImpl smtpConfiguration = new SMTPConfigurationImpl();
        smtpConfiguration.setSoftwareName(properties.getSoftwareName());
        SMTPProtocolHandlerChain chain = new SMTPProtocolHandlerChain(new NoopMetricFactory());
        chain.addAll(0, handlers);
        chain.wireExtensibleHandlers();

        Protocol protocol = new SMTPProtocol(chain, smtpConfiguration);

        server = new NettyServer.Factory(new HashedWheelTimer())
                .protocol(protocol)
                .build();
        server.setListenAddresses(new InetSocketAddress(properties.getPort()));
        server.setTimeout(properties.getTimeout());
        server.bind();
        log.info("SMTP Server started on port: {}", properties.getPort());
    }


    public void stop() {
        server.unbind();
        log.info("SMTP Server stopped on port: {}", properties.getPort());
    }

}
