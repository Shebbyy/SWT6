package swt6.spring.worklog.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@Slf4j
@RequestMapping(value = "/worklog", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeRestController {
    // @SLF4J = private static final logger = ...
    public EmployeeRestController() {
        log.info(this.getClass().getName() + " constructed");
    }

    @GetMapping(value = "hello", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        log.info(this.getClass().getName() + "::hello()");
        return "Hello from " + this.getClass().getName();
    }
}
