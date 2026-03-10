package swt6.orm.domain;

import jakarta.persistence.*;
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
public class LogbookEntry implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String activity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private Employee employee;

    public LogbookEntry(String activity, LocalDateTime startTime, LocalDateTime endTime) {
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
