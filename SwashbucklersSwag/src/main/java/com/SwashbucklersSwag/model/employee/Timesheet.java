package com.SwashbucklersSwag.model.employee;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a timesheet
 *
 * @author Daniel Bernier
 */

@Entity
@Table(name = "timesheets")
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

    public Timesheet(){}

    public Timesheet(int timesheetId, Timestamp clockIn, Timestamp clockOut, Employee employee) {
        this.timesheetId = timesheetId;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.employee = employee;
    }

    public int getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(int timesheetId) {
        this.timesheetId = timesheetId;
    }

    public Timestamp getClockIn() {
        return clockIn;
    }

    public void setClockIn(Timestamp clockIn) {
        this.clockIn = clockIn;
    }

    public Timestamp getClockOut() {
        return clockOut;
    }

    public void setClockOut(Timestamp clockOut) {
        this.clockOut = clockOut;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timesheet timesheet = (Timesheet) o;
        return timesheetId == timesheet.timesheetId && Objects.equals(clockIn, timesheet.clockIn) && Objects.equals(clockOut, timesheet.clockOut) && Objects.equals(employee, timesheet.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timesheetId, clockIn, clockOut, employee);
    }

    @Override
    public String toString() {
        return "Timesheet{" +
                "timesheetId=" + timesheetId +
                ", clockIn=" + clockIn +
                ", clockOut=" + clockOut +
                ", employee=" + employee +
                '}';
    }
}
