package swt6.spring.worklog.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.jdbc.EmployeeDaoJdbc;

import javax.sql.DataSource;

@Configuration
@PropertySource("jdbc.properties")
public class JdbcConfig {

  @Value("${database.driverClassName}")
  private String driverClassName;

  @Value("${database.url}")
  private String url;

  @Value("${database.username}")
  private String username;

  @Value("${database.password}")
  private String password;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean(destroyMethod = "close")
  public HikariDataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

	// ========================= DAO DEFINITIONS ================================

    @Bean
    public JdbcTemplate JdbcTemplate(DataSource dataSource) {
      return new JdbcTemplate(dataSource);
    }

	// ====================== BUSINESS OBJECT DEFINIONS =========================

    @Bean
    public EmployeeDao employeeDaoJdbc(JdbcTemplate template) {
        EmployeeDaoJdbc employeeDaoJdbc = new EmployeeDaoJdbc();
        employeeDaoJdbc.setJdbcTemplate(template);

        return employeeDaoJdbc;
    }
}