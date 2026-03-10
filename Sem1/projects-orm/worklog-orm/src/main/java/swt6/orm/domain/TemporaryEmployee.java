package swt6.orm.domain;

import java.io.Serial;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("TE")
@Getter 
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class TemporaryEmployee extends Employee {
  @Serial
  private static final long serialVersionUID = 1L;

  private String            renter;
  private double            hourlyRate;
  private LocalDate         startDate;
  private LocalDate         endDate;

  public TemporaryEmployee(String firstName, String lastName, LocalDate dateOfBirth) {
    super(firstName, lastName, dateOfBirth);
  }
}
