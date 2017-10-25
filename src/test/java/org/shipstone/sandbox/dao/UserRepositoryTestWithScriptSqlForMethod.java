package org.shipstone.sandbox.dao;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shipstone.sandbox.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * @author François Robert
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTestWithScriptSqlForMethod {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void with_new_db_list_mustBe_empty() {
    List<User> userList = userRepository.findAll();
    assertNotNull(userList);
    assertThat(userList).isNotNull().isEmpty();
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql"})
  public void with_new_db_initialized_list_mustBe_not_empty() {
    List<User> userList = userRepository.findAll();
    assertNotNull(userList);
    assertThat(userList).isNotNull().isNotEmpty().size().isEqualTo(2);
  }

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  public void search_one_person_by_name() {
    List<User> userList = userRepository.findAll();
    assertThat(userList).isNotNull().isNotEmpty().size().isEqualTo(2);
    User user = userRepository.findByLastname("Stark");
    assertThat(user).isNotNull();
  }

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  @Transactional()
  public void insert_one_person_should_list_size_must_be_3() {
    userRepository.saveAndFlush(new User(null, "machin", "truc", null));
    List<User> userList = userRepository.findAll();
    assertThat(userList).isNotNull().isNotEmpty().size().isEqualTo(3);
  }

  @Test
  @Transactional
  public void use_paginate_mustbe_done() {
    for (int idx = 1; idx <= 10; idx++) {
      userRepository.save(new User(null, "firstname" + idx, "lastname" + idx, null));
    }
    Pageable pageable = new PageRequest(2, 2);
    Page<User> userPage = userRepository.findAll(pageable);
    assertThat(userPage).isNotNull().size().isEqualTo(2);
  }

  @Test
  @Transactional
  public void use_paginate_and_sort_mustbe_done() {
    for (int idx = 1; idx <= 100; idx++) {
      userRepository.save(new User(null, "firstname" + idx, "lastname" + idx, null));
    }
    Sort sort = new Sort(Sort.Direction.DESC, "lastname");
    Pageable pageable = new PageRequest(2, 2, sort);
    Page<User> userPage = userRepository.findAll(pageable);
    assertThat(userPage).isNotNull().size().isEqualTo(2);
    assertThat(userPage.getContent().get(0)).isNotNull()
        .isExactlyInstanceOf(User.class)
        .isNot(new Condition<User>() {
          @Override
          public boolean matches(User value) {
            return value.getFirstname().equalsIgnoreCase("firstname96");
          }
        })
        .is(new Condition<User>() {
          @Override
          public boolean matches(User value) {
            return value.getFirstname().equalsIgnoreCase("firstname95");
          }
        })
    ;
  }

}
