

import networking.ClientWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ServiceProxy;
import services.Services;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:config.xml");
        Properties props = (Properties) factory.getBean("props");
        ClientWorker client = new ClientWorker(props.getProperty("hostname"), Integer.parseInt(props.getProperty("port")));
        Services services = new ServiceProxy(client);
        MainFX mainFX = MainFX.getInstance(services);
        mainFX.run();
        client.closeConnection();


    }
}
