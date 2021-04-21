package com.sk8.swashbucklers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Timesheet;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;
import java.util.function.Function;

/**
 * Timesheet data transfer object for timesheets {@link com.sk8.swashbucklers.service.TimesheetService}
 * @author Edson Rodriguez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetDTO {

    @Positive
    private int timesheetId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Timestamp clockIn;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp clockOut;
    @NonNull
    private Employee employee;

    public static Function<Timesheet,TimesheetDTO> timesheetToDTO(){
        return (timesheet) -> {
            Assert.notNull(timesheet.getEmployee());
            timesheet.getEmployee().setPassword("");
            return new TimesheetDTO(
                    timesheet.getTimesheetId(),
                    timesheet.getClockIn(),
                    timesheet.getClockOut(),
                    timesheet.getEmployee());
        };
    }
}
