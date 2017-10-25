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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.shipstone.sandbox.dao.DataSpecification.ValueOperation.EQUAL;
import static org.shipstone.sandbox.dao.DataSpecification.ValueOperation.LIKE;

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

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  @Transactional()
  public void use_query_like() {
    Pageable pageable = new PageRequest(0, 2);
    Page<User> userPage = userRepository.findByLastnameContaining("Sta", pageable);
    assertThat(userPage.getContent())
        .isNotNull()
        .size().isEqualTo(1)
        .returnToIterable()
        .is(new Condition<List<? extends User>>() {
          @Override
          public boolean matches(List<? extends User> value) {
            return value.get(0).getFirstname().equalsIgnoreCase("anthony");
          }
        });
  }

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  @Transactional()
  public void use_specification_mustbe_done() {
    /*
    Utilisation du login de génération de source pour les "root" entity

                <plugin>
                <groupId>org.bsc.maven</groupId>
                <artifactId>maven-processor-plugin</artifactId>
                <version>2.2.4</version>
                <executions>
                    <execution>
                        <id>process</id>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <phase>generate-sources</phase>
                        <configuration>
                            <processors>
                                <processor>org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor</processor>
                            </processors>
                        </configuration>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.hibernate</groupId>
                        <artifactId>hibernate-jpamodelgen</artifactId>
                        <version>4.3.8.Final</version>
                    </dependency>
                </dependencies>
            </plugin>

     */
    Specification<User> userSpecification = buildUserSearch("Stark", null);
    List<User> userList = userRepository.findAll(userSpecification);
    assertThat(userList)
        .isNotNull()
        .size().isEqualTo(1).returnToIterable()
        .is(new Condition<List<? extends User>>() {
          @Override
          public boolean matches(List<? extends User> value) {
            return value.get(0).getFirstname().equals("Anthony");
          }
        });
  }

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  @Transactional()
  public void use_DataSpecification_mustbe_done() {
    /*
    Peut être pas obligé de faire processing (plugin) des entité avec ce truc... :)
    Effectivement, le processing d'entité n'est pas neccessaire !!
     */
    String _lastname = "Sta";
    String _firstname = "Anthony";
    // TODO: 26/10/2017 A tester si c'est la propriété du POJO ou le champs SQL
    List<User> userList = userRepository.findAll(
        new DataSpecification<User>()
            .condition("lastname", LIKE, _lastname)
            .condition("firstname", EQUAL, _firstname)
    );
    assertThat(userList)
        .isNotNull()
        .size().isEqualTo(1).returnToIterable()
        .is(new Condition<List<? extends User>>() {
          @Override
          public boolean matches(List<? extends User> value) {
            return value.get(0).getFirstname().equals("Anthony");
          }
        });
  }

  private Specification<User> buildUserSearch(String lastname, String firstname) {
    return new Specification<User>() {
      @Override
      public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = null;
        // necessite l'ajout du plugin de processing d'entité
        // predicate = buildPredicate(predicate, lastname, criteriaBuilder, criteriaBuilder.equal(root.get(User_.lastname), lastname));
        // predicate = buildPredicate(predicate, firstname, criteriaBuilder, criteriaBuilder.equal(root.get(User_.firstname), firstname));
        return predicate;
      }
    };
  }

  private Predicate buildPredicate(
      Predicate initialPredicate,
      Object value,
      CriteriaBuilder criteriaBuilder,
      Predicate valuePredicate
  ) {
    if (value != null) {
      return initialPredicate == null ? valuePredicate : criteriaBuilder.and(valuePredicate, initialPredicate);
    }
    return initialPredicate;
  }

  @Test
  @Sql("/datasets/PersonRepositoryTestWithScriptSqlForMethod-01.sql")
  @Transactional()
  public void use_specificationBuilder_mustbe_done() {
    /*
    CE TEST PLANTE !!!

    A voir pour faire un builder plus pratique !!
     */
    /*
    String _lastname = "Sta";
    String _firstname = "Anthony";
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
    Root<User> userRoot = userCriteriaQuery.from(User.class);
    SpecificationBuilder<User> userSpecificationBuilder = new SpecificationBuilder<>();
    Specification<User> userSpecification = userSpecificationBuilder
        .predicate(_lastname, criteriaBuilder.like(userRoot.get(User_.lastname), _lastname))
        .predicate(_firstname, criteriaBuilder.equal(userRoot.get(User_.firstname), _firstname))
        .build();
    List<User> userList = userRepository.findAll(userSpecification);
    assertThat(userList)
        .isNotNull()
        .size().isEqualTo(1).returnToIterable()
        .is(new Condition<List<? extends User>>() {
          @Override
          public boolean matches(List<? extends User> value) {
            return value.get(0).getFirstname().equals("Anthony");
          }
        });
        */
  }


}
