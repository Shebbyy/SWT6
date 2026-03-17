package swt6.spring.basics.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
// Register Beans with Component Scan
// Base Package for Component Scan is the Package of the Config Class
@ComponentScan
@EnableAspectJAutoProxy
public class AopConfig {
}
