//		/clock
//                GET		/all
//                (header parameters)
//                authorization = JWT
//
//                (path parameters)
//                [none]
//
//                (optional query string parameters)
//                page=INTEGER
//                offset=[5, 10, 25, 50, 100]
//                sortby="STRING"
//                order=["ASC"|"DESC"]
//
//                pagenumber = integer, which page to select Default: 1
//                offset = integer, how many items to display per page [5, 10, 25, 50, 100] Default: 25
//                sortby = the name of the property to sort by Default: timesheetId
//                order = either ASC for ascending order or DESC for descending order Default: ASC
//
//                Example(s):
//                /employee/clock/all
//                /employee/clock/all?page=2&offset=10
//                /employee/clock/all?page=2&offset=10&sortby="employeeId"&order="ASC"
//
//                GET		/id
//                (header parameters)
//                authorization = JWT
//
//                (required path parameters)
//                /{id}
//
//                (query string parameters)
//                [none]
//
//                /{id} = integer, the id of the timesheet being requested
//
//                Example(s):
//                /employee/clock/id/14
//
//                GET		/employee-id
//                (header parameters)
//                authorization = JWT
//
//                (required path parameters)
//                /{id}
//
//                (optional query string parameters)
//                page=INTEGER
//                offset=[5, 10, 25, 50, 100]
//                sortby="STRING"
//                order=["ASC"|"DESC"]
//
//                /{id} = integer, the employee id of the employee's timesheets being requested
//                pagenumber = integer, which page to select Default: 1
//                offset = integer, how many items to display per page [5, 10, 25, 50, 100] Default: 25
//                sortby = the name of the property to sort by Default: timesheetId
//                order = either ASC for ascending order or DESC for descending order Default: ASC
//
//                Example(s):
//                /employee/clock/employee-id/12
//                /employee/clock/employee-id/55?page=1&offset=50
//                /employee/clock/employee-id/3?page=4&offset=5&sortby="clockIn"&order="DESC"
//
//                POST	/in
//                (header parameters)
//                [none]
//
//                (path parameters)
//                [none]
//
//                (string parameters)
//                [none]
//
//                Example(s):
//                /employee/clock/in
//
//                (Body JSON format)
//                {
//                "email" : "STRING",
//                "password" : "STRING"
//                }
//
//                POST	/out
//                (header parameters)
//                [none]
//
//                (path parameters)
//                [none]
//
//                (string parameters)
//                [none]
//
//                Example(s):
//                /employee/clock/out
//
//                (Body JSON format)
//                {
//                "email" : "STRING",
//                "password" : "STRING"
//                }
//
//                POST	/update
//                (header parameters)
//                authorization = JWT
//
//                (path parameters)
//                [none]
//
//                (string parameters)
//                [none]
//
//                Example(s):
//                /employee/clock/update
//
//                (Body JSON format)
//                {
//                "timesheetId" : INTEGER,
//                "clockIn" : TIMESTAMP,
//                "clockOut" : TIMESTAMP
//                }
//