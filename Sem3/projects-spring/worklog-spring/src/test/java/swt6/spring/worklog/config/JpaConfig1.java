package swt6.spring.worklog.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import swt6.spring.worklog.dao.EmployeeDao;
import swt6.spring.worklog.dao.EmployeeRepository;
import swt6.spring.worklog.dao.GenericDao;
import swt6.spring.worklog.dao.jpa.EmployeeDaoJpa;
import swt6.spring.worklog.logic.WorkLogService;
import swt6.spring.worklog.logic.WorkLogServiceImpl;
import swt6.spring.worklog.ui.WorkLogViewModel;
import swt6.spring.worklog.ui.WorkLogViewModelImpl;
import swt6.util.advice.JpaInterceptor;

@Configuration
@Import(JpaDataSourceConfig.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = GenericDao.class)
@EnableAspectJAutoProxy
public class JpaConfig1 {

  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
  }

  @Bean
  public TransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
    var transactionManager = new JpaTransactionManager();

    // Can be omitted, a single EntityManagerFactory Bean is found automatically
    transactionManager.setEntityManagerFactory(emf.getObject());

    return transactionManager;
  }

  @Bean
  public JpaInterceptor jpaInterceptor(LocalContainerEntityManagerFactoryBean emFact) {
      return new JpaInterceptor(emFact.getObject());
  }

  // ============================== DATA ACCESS LAYER =============================
    @Bean
    public EmployeeDao employeeDaoJpa() {
      return new EmployeeDaoJpa();
    }

  //============================= BUSINESS LOGIC LAYER ============================

    @Bean
    public WorkLogService workLogService(EmployeeDao employeeDao) {
      return new WorkLogServiceImpl(employeeDao);
    }
  // ============================== PRESENTATION LAYER  ===========================

    @Bean
    public WorkLogViewModel workLogViewModel(WorkLogService workLogService) {
      return new WorkLogViewModelImpl(workLogService);
    }
}