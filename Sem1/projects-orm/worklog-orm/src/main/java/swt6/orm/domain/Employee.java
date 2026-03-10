package swt6.orm.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
// @Setter(AccessLevel.PRIVATE) alternative for Access-Level Configuration
@NoArgsConstructor
@ToString
public class Employee implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    // @Column
    private LocalDate dateOfBirth;
    private String email;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    // mappedBy -> Field in Child-Class which represents the current class, eg. employee in LogbookEntry
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL) // persist/merge/remove all forwarded
    private Set<LogbookEntry> logbookEntries = new HashSet<>();


    public Employee(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public void addLogbookEntry(LogbookEntry logbookEntry) {
        if (logbookEntry.getEmployee() != null) {
            // remove existing link to allow for new one
            logbookEntry.getEmployee().getLogbookEntries().remove(logbookEntry);
        }

        // set logbook entry link
        logbookEntry.setEmployee(this);
        logbookEntries.add(logbookEntry);
    }
}
