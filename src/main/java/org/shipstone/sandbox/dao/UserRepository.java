package org.shipstone.sandbox.dao;

import org.shipstone.sandbox.entity.Person;
import org.shipstone.sandbox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author François Robert
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByLastname(String lastname);
}
