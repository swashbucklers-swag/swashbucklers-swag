package com.sk8.swashbucklers.DTO;

import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.model.employee.Timesheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Timestamp;

/**
 * Tests for {@link com.sk8.swashbucklers.dto.TimesheetDTO}
 * @author Edson Rodriguez
 */

@SpringBootTest
public class TimesheetDTOTest {

    @Test
    void whenConvertingToDTO_DTOFieldsMatchOriginalObject(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);
        TimesheetDTO d = TimesheetDTO.timesheetToDTO().apply(timesheet);

        Assertions.assertEquals(0,d.getTimesheetId());
        Assertions.assertEquals(ci,d.getClockIn());
        Assertions.assertEquals(co,d.getClockOut());
        Assertions.assertEquals(employee,d.getEmployee());
    }
}
