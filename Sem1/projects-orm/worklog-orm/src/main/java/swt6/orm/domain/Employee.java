package swt6.orm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
// V1 -> Separate Table per Child, containing all Properties of Parent -> Connected via Union ALL
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// V2 -> Child-Class Tables only contain additional properties -> Connected via Join
//@Inheritance(strategy = InheritanceType.JOINED)
// V3 -> Single Table with Discriminator Column
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "employee_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("E")
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
    // Version 1 nothing, stored in one column serialized
    // Version 2 OneToOne
    //@OneToOne(cascade = CascadeType.ALL)
    // Version 3, stored as additional columns in Employee Table, Embedded
    @Embedded
    // @AttributeOverride() -> Allows Column-Name definition to avoid overlaps
    @AttributeOverride(name = "zipCode", column = @Column(name="address_zipCode"))
    @AttributeOverride(name = "city", column = @Column(name="address_city"))
    @AttributeOverride(name = "street", column = @Column(name="address_street"))
    private Address address;

    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    // mappedBy -> Field in Child-Class which represents the current class, eg. employee in LogbookEntry
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // persist/merge/remove all forwarded
    @org.hibernate.annotations.Fetch(FetchMode.SELECT)
    // fetching strategies:
    //   [FetchMode.JOIN]    FetchType.EAGER   1 join, eager fetch
    //   FetchMode.SELECT    FetchType.EAGER   2 selects, eager fetch
    //   [FetchMode.SELECT]  [FetchType.LAZY]  2 selects, lazy fetch
    //   FetchMode.JOIN      [FetchType.LAZY]  contradictory
    // Default fetch strategy: LAZY
    private Set<LogbookEntry> logbookEntries = new HashSet<>();

    // Save without separate Entity
    @ElementCollection
    @CollectionTable(name="EMPL_PHONES")
    @Column(name="PHONE_NR")
    @ToString.Exclude
    @Setter(AccessLevel.PRIVATE)
    private Set<String> phones = new HashSet<>();


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

    public void addPhone(String phone) {
        phones.add(phone);
    }
}
