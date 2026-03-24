package swt6.spring.worklog.ui;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import swt6.spring.worklog.domain.Employee;
import swt6.spring.worklog.logic.WorkLogService;
import swt6.util.annotation.ViewModel;

// WorkLogViewModelImpl surrounds all of its methods with a JpaInterceptor that
// that keeps the EntityManager open. This is important when lazily loaded child
// objects are accessed. Not doing so results in a LazyLoadingException.
@RequiredArgsConstructor
@ViewModel
public class WorkLogViewModelImpl implements WorkLogViewModel {
  
    // @NotNull alternative to final for Lombok RequiredArgsConstructor
    private final WorkLogService workLogService;

  
  @Override
  public void saveEmployees(Employee... employees) {
    for (Employee e : employees)
      workLogService.syncEmployee(e);
  }

  @Override
  public void findAll() {
    for (Employee e : workLogService.findAllEmployees()) {
      System.out.println(e);
      e.getLogbookEntries().forEach(entry -> System.out.println("   " + entry));
    }
  }
}
