package org.shipstone.sandbox.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author François Robert
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTestWithSql {

  @ShipstoneLogger
  private Logger logger;

  @Test
  public void test_logger_should_be_ok() {
    logger.info("coucou");
  }
}

