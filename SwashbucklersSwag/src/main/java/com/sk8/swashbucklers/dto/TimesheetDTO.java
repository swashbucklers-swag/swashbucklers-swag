package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.employee.Timesheet;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.function.Function;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetDTO {

    private int timesheetId;
    private Timestamp clockIn;
    private Timestamp clockOut;
    private int employeeId;
    @Email
    private String email;
    @Size(min = 1, max = 255)
    private String name;

    public static Function<Timesheet,TimesheetDTO> timesheetToDTO(){
        return (timesheet) -> {
            Assert.notNull(timesheet.getEmployee());
            return new TimesheetDTO(
                    timesheet.getTimesheetId(),
                    timesheet.getClockIn(),
                    timesheet.getClockOut(),
                    timesheet.getEmployee().getEmployeeId(),
                    timesheet.getEmployee().getEmail(),
                    timesheet.getEmployee().getFirstName());
        };
    }
}
