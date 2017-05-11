package org.shipstone.sandbox.dao;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shipstone.sandbox.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author François Robert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    SqlScriptsTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup(PersonRepositoryTestWithDBUnit.DATASETS_PERSON_01_XML)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {PersonRepositoryTestWithDBUnit.DATASETS_PERSON_01_XML})
public class PersonRepositoryTestWithDBUnit {

  static final String DATASETS_PERSON_01_XML = "/datasets/person-01.xml";

  @Autowired
  private PersonRepository personRepository;

  @Test
  public void personDao_should_be_injected() {
    assertNotNull("le DAO PersonRepository doit être instancié");
  }

  @Test
  public void should_get_person_by_id() {
    Person person = personRepository.findOne(1L);
    assertNotNull("La personne ne doit pas être null", person);
    Person personExpected = new Person();
    personExpected.setId(1L);
    assertEquals("La personne doit être retrouvé", personExpected, person);
  }
}
