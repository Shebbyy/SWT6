package swt6.spring.worklog.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID extends Serializable> {
    Optional<T> findById(ID id);
    List<T> findAll();
    void insert(T entity);
    T merge(T entity);
}
