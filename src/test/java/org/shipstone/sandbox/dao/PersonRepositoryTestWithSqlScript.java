package org.shipstone.sandbox.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shipstone.sandbox.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author François Robert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    SqlScriptsTestExecutionListener.class
})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {PersonRepositoryTestWithSqlScript.DATASETS_PERSON_01_SQL})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {PersonRepositoryTestWithSqlScript.DATASETS_CLEAR_ALL_SQL})
public class PersonRepositoryTestWithSqlScript {

  public static final String DATASETS_PERSON_01_SQL = "/datasets/person-01.sql";
  public static final String DATASETS_CLEAR_ALL_SQL = "/datasets/clear-all.sql";

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
    assertEquals("Le nom de la personne renvoyé doit être exacte", "Stark", person.getLastname());
  }

  @Test
  public void should_get_person_by_lastname_with_like() {
    Pageable pageable = new PageRequest(0, 2);
    List<Person> personList = personRepository.getByLastName("Sta", pageable);
    assertTrue("LA liste doit contenir 1 personn", personList.size() == 1);
  }

  @Test
  public void should_return_person_with_mainAddress() {
    Person person = personRepository.findOne(1l);
    assertNotNull(person.getMainAddress());
    assertEquals("L'identifiant de l'adresse doit être le bon", new Long(1L), person.getMainAddress().getId());
  }

  @Test
  public void create_person_should_be_done() {
    Person person = new Person(null, "toto", "titi", null);
    Person otherPerson = personRepository.saveAndFlush(person);
    assertNotNull(otherPerson);
    assertEquals(new Long(1L), otherPerson.getId());
    person = new Person(null, "toto", "titi", null);
    otherPerson = personRepository.saveAndFlush(person);
    assertEquals(new Long(2L), otherPerson.getId());
  }

}
