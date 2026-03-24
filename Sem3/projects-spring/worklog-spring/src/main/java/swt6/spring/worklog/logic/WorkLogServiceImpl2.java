package swt6.spring.worklog.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.EmployeeRepository;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

// Field needs to be final for RequiredArgsConstructor to set it
@RequiredArgsConstructor
@Transactional
@Service
@Primary // can be used to control which impl is used for the interface
public class WorkLogServiceImpl2 implements WorkLogService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee syncEmployee(Employee employee) {
        // v1 use utility class to wrap BL code with a transaction
        //JpaUtil.executeInTransaction(emFactory, () -> {
        //    return employeeDao.merge(employee);
        //});

        // v2 use @Transactional to add transactions to BL
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }
}
