package org.shipstone.sandbox.dao;

import org.shipstone.sandbox.entity.Person;
import org.shipstone.sandbox.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author François Robert
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByLastname(String lastname);

  Page<User> findByLastnameContaining(String lastname, Pageable pageable);

  List<User> findAll(Specification<User> userSpecification);
}
