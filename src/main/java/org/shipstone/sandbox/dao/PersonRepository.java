package org.shipstone.sandbox.dao;

import org.shipstone.sandbox.entity.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author François Robert
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

  @Query(
      value = "select p from Person p where p.lastname like :lastname%",
      countQuery = "select count(p) from Person p where p.lastname like :lastname%"
  )
  List<Person> getByLastName(
      @Param("lastname") String lastname,
      Pageable pageable
  );
}
