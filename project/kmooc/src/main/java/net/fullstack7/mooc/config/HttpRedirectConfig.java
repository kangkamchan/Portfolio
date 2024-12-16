package net.fullstack7.mooc.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class HttpRedirectConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
  @Override
  public void customize(TomcatServletWebServerFactory factory) {
      factory.addAdditionalTomcatConnectors(createHttpConnector());
  }

  private Connector createHttpConnector() {
      Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
      connector.setPort(8080);
      connector.setScheme("http");
      connector.setSecure(false);
      connector.setRedirectPort(8443);
      return connector;
  }
}