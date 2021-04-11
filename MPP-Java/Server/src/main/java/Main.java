import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import networking.ServerWorker;
import services.Services;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:config.xml");
        Services services = (Services) factory.getBean("services");
        Properties props = (Properties) factory.getBean("serverProps");
        ServerWorker server = new ServerWorker(Integer.parseInt(props.getProperty("port")));
        server.startServer(services);

    }
}
