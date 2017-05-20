package org.shipstone.sandbox.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shipstone.sandbox.configuration.annotation.ShipstoneLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private PersonService personService;

  @Test
  public void service_should_be_injected() {
    Assert.assertNotNull("Le service des personnes doit être injecté", personService);
  }
}

