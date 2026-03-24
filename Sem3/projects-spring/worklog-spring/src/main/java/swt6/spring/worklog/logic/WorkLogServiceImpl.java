package swt6.spring.worklog.logic;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

// Field needs to be final for RequiredArgsConstructor to set it
@RequiredArgsConstructor
@Transactional
public class WorkLogServiceImpl implements WorkLogService {
    private final EmployeeDao employeeDao;

    @Override
    public Employee syncEmployee(Employee employee) {
        // v1 use utility class to wrap BL code with a transaction
        //JpaUtil.executeInTransaction(emFactory, () -> {
        //    return employeeDao.merge(employee);
        //});

        // v2 use @Transactional to add transactions to BL
        return employeeDao.merge(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAllEmployees() {
        return employeeDao.findAll();
    }
}
