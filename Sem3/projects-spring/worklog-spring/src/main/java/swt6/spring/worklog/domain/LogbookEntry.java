package swt6.spring.worklog.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LogbookEntry implements Serializable, Comparable<LogbookEntry> {

  @Id @GeneratedValue
  private Long          id;

  private String        activity;

  private LocalDateTime startTime;

  private LocalDateTime endTime;
  
  @ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE}, fetch=FetchType.EAGER)
  @ToString.Exclude
  private Employee employee;
  
  public LogbookEntry(String activity, LocalDateTime start, LocalDateTime end) {
    this.activity = activity;
    this.startTime = start;
    this.endTime = end;
  }
  
  public void attachEmployee(Employee employee) {
    // If this entry is already linked to some employee,
    // remove this link.
    if (this.employee != null)
      this.employee.getLogbookEntries().remove(this);
    
    // Add a bidirectional link between this entry and employee.
    if (employee != null)
       employee.getLogbookEntries().add(this);
    this.employee = employee;
  }

  public void detachEmployee() {
    if (this.employee != null)
      this.employee.getLogbookEntries().remove(this);
    
    this.employee = null;
  }

  public int compareTo(LogbookEntry entry) {
    return this.startTime.compareTo(entry.startTime);
  }
}
