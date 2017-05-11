package org.shipstone.sandbox.dao;

import org.shipstone.sandbox.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author François Robert
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
