package org.shipstone.sandbox.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author François Robert
 */
public class PersonTest {

  @Test
  public void create_single_person() {
    Person person = new Person();
    Assert.assertNotNull("La classe personne doit être initialisé", person);
  }

}
