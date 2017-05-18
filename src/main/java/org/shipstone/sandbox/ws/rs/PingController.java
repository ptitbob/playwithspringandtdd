package org.shipstone.sandbox.ws.rs;

import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author François Robert
 */
@RestController("ping")
public class PingController {

  @ShipstoneLogger
  private Logger logger;

  @Autowired
  public PingController() {
  }

  @GetMapping
  public String ping() {
    logger.info("ping pong process");
    return "pong";
  }

}
