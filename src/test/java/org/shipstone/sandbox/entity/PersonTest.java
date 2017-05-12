package org.shipstone.sandbox.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author François Robert
 */
public class PersonTest {

  @Test
  public void create_single_person() {
    Person person = new Person();
    Assert.assertNotNull("La classe personne doit être initialisé", person);
  }

  @Test
  public void primitive_getter_and_setter_ok() {
    Person person = new Person();
    person.setId(1L);
    person.setFirstname("toto");
    person.setLastname("titi");
    assertEquals("L'identofiant est mal enregistré", new Long(1), person.getId());
    assertEquals("le nom est mal enregistré", "titi", person.getLastname());
    assertEquals("Le prenom est mal enregistré", "toto", person.getFirstname());
  }

  @Test
  public void address_filled() {
    Person person = new Person();
    Address address = new Address();
    address.setId(10l);
    person.setMainAddress(address);
    assertNotNull("l'adresse de la personne est null", person.getMainAddress());
    assertEquals("L'adresse est correctement assigné", new Long(10), person.getMainAddress().getId());
  }
}
