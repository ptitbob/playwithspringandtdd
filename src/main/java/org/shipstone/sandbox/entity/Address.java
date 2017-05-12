package org.shipstone.sandbox.entity;

import lombok.Data;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author François Robert
 */
@Entity
@Data
public class Address {

  @Id
  @Column(name = "address_id")
  private Long id;

}
