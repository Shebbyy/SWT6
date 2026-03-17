package swt6.spring.basics.aop.logic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
  private Long     id;
  private String   firstName;
  private String   lastName;
  
   public Employee(String firstName, String lastName) {
    this(null, firstName, lastName);
  }


  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (id != null)
      sb.append(id + ": ");
    sb.append(lastName + ", " + firstName);
    
    return sb.toString();
  }
}
