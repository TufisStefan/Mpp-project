import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import repository.ExcursionORMRepository;
import services.Services;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        /*ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:config.xml");
        Services services = (Services) factory.getBean("services");
        Properties props = (Properties) factory.getBean("serverProps");
        ServerWorker server = new ServerWorker(Integer.parseInt(props.getProperty("port")));
        server.startServer(services);*/
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            System.out.println(sessionFactory);

        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
        ExcursionORMRepository.setSessionFactory(sessionFactory);
        ExcursionORMRepository repository = new ExcursionORMRepository();
        //System.out.println(repository.findAll());
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:config.xml");
    }
}
