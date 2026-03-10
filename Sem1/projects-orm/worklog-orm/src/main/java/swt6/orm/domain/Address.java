package swt6.orm.domain;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Version 2 OneToOne
//@Entity
// Version 3 in Table
@Embeddable
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Address implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // Id Unnecessary for embeddable
  //@Id
  //@GeneratedValue
  //private Long     id;
  private String   zipCode;
  private String   city;
  private String   street;

  public Address(String zipCode, String city, String street) {
    this.zipCode = zipCode;
    this.city = city;
    this.street = street;
  }
}
