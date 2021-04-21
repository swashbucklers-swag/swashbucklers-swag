package com.sk8.swashbucklers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

/**
 * Timesheet data transfer object for updating timesheets {@link com.sk8.swashbucklers.service.TimesheetService}
 * @author Edson Rodriguez
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTimesheetDTO {

    @Positive
    private int timesheetId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Timestamp clockIn;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @NonNull
    private Timestamp clockOut;
}
