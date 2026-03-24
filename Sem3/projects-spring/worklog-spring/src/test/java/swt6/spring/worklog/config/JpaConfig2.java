package swt6.spring.worklog.config;

import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.GenericDao;
import swt6.spring.worklog.dao.jpa.EmployeeDaoJpa;
import swt6.spring.worklog.logic.WorkLogService;
import swt6.spring.worklog.logic.WorkLogServiceImpl;
import swt6.spring.worklog.ui.WorkLogViewModel;
import swt6.spring.worklog.ui.WorkLogViewModelImpl;
import swt6.util.advice.JpaInterceptor;

// V2, import automated instead of explicit declarations

@Configuration
@Import(JpaDataSourceConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = GenericDao.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses =
        {
                swt6.spring.worklog.dao.GenericDao.class,
                swt6.spring.worklog.logic.WorkLogService.class
        })
public class JpaConfig2 {

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  public TransactionManager transactionManager() {
    return new JpaTransactionManager();
  }
}