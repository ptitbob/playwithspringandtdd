package org.shipstone.sandbox.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author François Robert
 */
@Entity
@SequenceGenerator(
    name = "personSequence",
    sequenceName = "PERSON_SEQ"
)
@Data
public class Person {

  @Id
  @Column(name = "person_id")
  @GeneratedValue(generator = "personSequence")
  private Long id;


}
