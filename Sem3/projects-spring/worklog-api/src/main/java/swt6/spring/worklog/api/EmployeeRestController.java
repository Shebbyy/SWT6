package swt6.spring.worklog.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.dto.EmployeeDto;
import swt6.spring.worklog.logic.WorkLogService;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@RestController
@Controller
@Slf4j
@RequestMapping(value = "/worklog", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeRestController {
    // @SLF4J = private static final logger = ...

    private final ModelMapper mapper;

    private final WorkLogService workLog;
    public EmployeeRestController(ModelMapper mapper, WorkLogService workLog) {
        this.mapper = mapper;
        this.workLog = workLog;

        log.info(this.getClass().getName() + " constructed");
    }

    @GetMapping("employees")
    @Operation(summary = "Employee List", description = "Returns List of all stored employees")
    @ApiResponse(responseCode = "200", description = "Success")
    public List<EmployeeDto> getEmployees() {
        log.info(this.getClass().getName() + "::getEmployees()");

        var employees = workLog.findAllEmployees();
        Type listDtoType = new TypeToken<List<EmployeeDto>>(){}.getType();
        return mapper.map(employees, listDtoType);
    }

    @GetMapping("/employee/{id}")
    @Operation(summary = "Employee Data", description = "Returns detailed data for a given employee")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    public EmployeeDto getEmployeeById(@PathVariable("id") long id) {
        log.info(this.getClass().getName() + "::getEmployeeById()");
        Optional<Employee> empl = workLog.findEmployeeById(id);
        if (empl.isEmpty()) {
            throw new EmployeeNotFoundException(id);
        }

        return mapper.map(empl.get(), EmployeeDto.class);
    }

    @GetMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        log.info(this.getClass().getName() + "::hello()");
        return "Hello from " + this.getClass().getName();
    }
}
