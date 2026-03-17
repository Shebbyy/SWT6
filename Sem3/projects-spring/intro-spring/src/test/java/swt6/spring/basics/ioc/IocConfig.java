package swt6.spring.basics.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import swt6.spring.basics.ioc.logic.WorkLogService;
import swt6.spring.basics.ioc.logic.javaconfig.WorkLogServiceImpl;
import swt6.spring.basics.ioc.util.ConsoleLogger;
import swt6.spring.basics.ioc.util.FileLogger;
import swt6.spring.basics.ioc.util.Log;
import swt6.spring.basics.ioc.util.Logger;

// Variant 3: Java-Based Configuration files
@Configuration
@ComponentScan(basePackages = "swt6.spring.basics.ioc.logic.javaconfig,swt6.spring.basics.ioc.util")
public class IocConfig {
    /*
    // Var 3.1: Register Beans explicitly in Config class
    @Bean
    public Logger consoleLogger() {
        return new ConsoleLogger();
    }

    @Bean @Log(Log.Type.FILE)
    public Logger fileLogger() {
        return new FileLogger();
    }

    //@Bean
    //public WorkLogService workLogService() {
        // explicit injection of dependency
        // spring uses singleton instead of recreating new instances
        //return new WorkLogServiceImpl(consoleLogger());
    //}

    // Method injection
    //@Bean
    //public WorkLogService workLogService(@Log Logger logger) {
    //    return new WorkLogServiceImpl(logger);
    //}

    @Bean
    public WorkLogService workLogService() {
        // Annotations of Logger Field would be used per default
        return new WorkLogServiceImpl();
    }*/

    // 3.2 Annotation based Bean registration
    //     and rely on Annotation Based Dependency Injection



}
