package swt6.spring.worklog.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Employee implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue(strategy=GenerationType.TABLE)
  private Long              id;
  
  private String            firstName;
  
  private String            lastName;

  private LocalDate         dateOfBirth;

  @OneToMany(mappedBy="employee", cascade=CascadeType.ALL, fetch=FetchType.LAZY,
             orphanRemoval=true)
  @ToString.Exclude
  private Set<LogbookEntry> logbookEntries = new HashSet<>();

  public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
  }

  public void addLogbookEntry(LogbookEntry entry) {
    if (entry.getEmployee() != null)
       entry.getEmployee().logbookEntries.remove(entry);

    this.logbookEntries.add(entry);
    entry.setEmployee(this);
  }

  public void removeLogbookEntry(LogbookEntry entry) {
    this.logbookEntries.remove(entry);
  }
}