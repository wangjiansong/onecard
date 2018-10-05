package task;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestQuartz {
	public static void main(String[] arg) {
        System.out.println("TestQuartz start.");
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-task.xml");
        //如果配置文件中将startQuertz bean的lazy-init设置为false 则不用实例化
//        context.getBean("taskManager");
        System.out.print("TestQuartz end..");
    }
}
