package com.SwashbucklersSwag.model.employee;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Represents a timesheet
 *
 * @author Daniel Bernier
 */

@Entity
@Table(name = "timesheets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timesheet {

    @Id
    @GeneratedValue
    @Column(name = "timesheet_id")
    private int timesheetId;
    @CreationTimestamp
    @Column(name = "clock_in", nullable = false)
    private Timestamp clockIn;
    @Column(name = "clock_out")
    private Timestamp clockOut;
    @ManyToOne
    @Column(name = "employee_id", nullable = false)
    private Employee employee;
}
