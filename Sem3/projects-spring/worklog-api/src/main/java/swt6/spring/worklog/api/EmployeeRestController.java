package swt6.spring.worklog.api;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swt6.spring.worklog.dto.EmployeeDto;
import swt6.spring.worklog.logic.WorkLogService;

import java.lang.reflect.Type;
import java.util.List;

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
    public List<EmployeeDto> getEmployees() {
        log.info(this.getClass().getName() + "::getEmployees()");

        var employees = workLog.findAllEmployees();
        Type listDtoType = new TypeToken<List<EmployeeDto>>(){}.getType();
        return mapper.map(employees, listDtoType);
    }

    @GetMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        log.info(this.getClass().getName() + "::hello()");
        return "Hello from " + this.getClass().getName();
    }
}
