package org.shipstone.sandbox.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * @author François Robert
 */
@Entity
@SequenceGenerator(
    name = "personSequence",
    sequenceName = "PERSON_SEQ",
    allocationSize = 1
)
@Data
public class Person {

  @Id
  @Column(name = "person_id")
  @GeneratedValue(generator = "personSequence")
  private Long id;

  @Column(length = 100)
  private String firstname;

  @Column(length = 100)
  private String lastname;

  @OneToOne
  @JoinColumn(name = "main_address_id", referencedColumnName = "address_id")
  private Address mainAddress;

}
