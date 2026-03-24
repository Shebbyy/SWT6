package swt6.spring.worklog.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDaoJpa implements EmployeeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Employee> findById(Long aLong) throws DataAccessException {
        return Optional.ofNullable(em.find(Employee.class, aLong));
    }

    @Override
    public List<Employee> findAll() throws DataAccessException {
        return em.createQuery("SELECT e FROM Employee e", Employee.class).getResultList();
    }

    @Override
    public void insert(Employee entity) throws DataAccessException {
        em.persist(entity);
    }

    @Override
    public Employee merge(Employee entity) throws DataAccessException {
        return em.merge(entity);
    }
}
