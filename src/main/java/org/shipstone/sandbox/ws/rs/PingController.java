package org.shipstone.sandbox.ws.rs;

import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author François Robert
 */
@RestController("ping")
public class PingController {

  @ShipstoneLogger(name = "PING PONG")
  private Logger logger;

  @ShipstoneLogger
  private Logger otherLogger;

  @Autowired
  public PingController() {
  }

  @PostConstruct
  public void initialization() {
    logger.info("Initialisation");
  }

  @GetMapping
  public String ping(HttpServletRequest request) {
    logger.info("ping pong process");
    otherLogger.info("arf !!");
    if (request != null) {
      logger.info("request not null");
    }
    return "pong";
  }

}
