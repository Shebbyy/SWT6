package swt6.spring.worklog.dao.jdbc;

import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.domain.Employee;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoJdbc implements EmployeeDao {

    @Setter
    private JdbcTemplate jdbcTemplate;

    protected static class EmployeeRowMapper implements RowMapper<Employee> {

        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            var e = new Employee();
            e.setId(rs.getLong(1));
            e.setFirstName(rs.getString(2));
            e.setLastName(rs.getString(3));
            e.setDateOfBirth(rs.getDate(4).toLocalDate());
            return e;
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        final String sql = "Select ID, FIRSTNAME, LASTNAME, DATEOFBIRTH From Employee WHERE ID=?";

        List<Employee> q = jdbcTemplate.query(sql, new EmployeeRowMapper(), id);

        if (q.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, q.size());
        }

        return q.isEmpty() ? Optional.empty() : Optional.of(q.getFirst());
    }

    @Override
    public List<Employee> findAll() {
        final String sql = "Select ID, FIRSTNAME, LASTNAME, DATEOFBIRTH From Employee";

        return new ArrayList<>(jdbcTemplate.query(sql, new EmployeeRowMapper()));
    }

    // Version 1: Data access code without Spring
  //public void insert(final Employee e) throws DataAccessException {
  //  final String sql =
  //    "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) "
  //    + "values (?, ?, ?)";
  //  try (Connection conn = dataSource.getConnection();
  //       PreparedStatement stmt = conn.prepareStatement(sql)) {
  //    stmt.setString(1, e.getFirstName());
  //    stmt.setString(2, e.getLastName());
  //    stmt.setDate(3, Date.valueOf(e.getDateOfBirth()));
  //    stmt.executeUpdate();
  //  }
  //  catch (SQLException ex) {
  //    System.err.println(ex);
  //  }
  //}


    // Version 2: JDBCTemplate
    //public void insert(final Employee e) throws DataAccessException {
    //    final String sql =
    //            "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) "
    //                    + "values (?, ?, ?)";
    //    jdbcTemplate.update(sql, ps -> {
    //        ps.setString(1, e.getFirstName());
    //        ps.setString(2, e.getLastName());
    //        ps.setDate(3, Date.valueOf(e.getDateOfBirth()));
    //    });
    //}

    // Version 3: JDBCTemplate simplified
    //public void insert(final Employee e) throws DataAccessException {
    //    final String sql =
    //            "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) "
    //                    + "values (?, ?, ?)";
    //    jdbcTemplate.update(sql,
    //        e.getFirstName(),
    //        e.getLastName(),
    //        Date.valueOf(e.getDateOfBirth())
    //    );
    //}

    // Version 4/final: JDBCTemplate simplified
    public void insert(final Employee e) throws DataAccessException {
        final String sql =
                "insert into EMPLOYEE (FIRSTNAME, LASTNAME, DATEOFBIRTH) "
                        + "values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, e.getFirstName());
            ps.setString(2, e.getLastName());
            ps.setDate(3, Date.valueOf(e.getDateOfBirth()));

            return ps;
        }, keyHolder);

        e.setId(keyHolder.getKey().longValue());

        jdbcTemplate.update(sql,
                e.getFirstName(),
                e.getLastName(),
                Date.valueOf(e.getDateOfBirth())
        );
    }

    @Override
    public Employee merge(Employee entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }

        return entity;
    }

    private void update(Employee empl) {
        final String sql = "update Employee set FIRSTNAME=?, LASTNAME=?, DATEOFBIRTH=? WHERE ID=?";

        jdbcTemplate.update(sql,
                empl.getFirstName(),
                empl.getLastName(),
                Date.valueOf(empl.getDateOfBirth()),
                empl.getId());
    }
}
