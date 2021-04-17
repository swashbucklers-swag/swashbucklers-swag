package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Timesheet data transfer object for updating timesheets {@link com.sk8.swashbucklers.service.TimesheetService}
 * @author Edson Rodriguez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimesheetDTO {

    private int timesheetId;
    private Timestamp clockIn;
    private Timestamp clockOut;
    private int employeeId;


}
