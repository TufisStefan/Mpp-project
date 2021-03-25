import domain.Excursion;
import domain.Reservation;
import domain.User;
import org.junit.Assert;
import org.junit.Test;
import repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Properties;

public class BDRepoTest {
    @Test
    public void testBD(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("test_config.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd_config "+e);
        }
        ExcursionRepository excursionRepository = new ExcursionDBRepository(props);
        UserRepository userRepository = new UserDBRepository(props);
        ReservationRepository reservationRepository = new ReservationDBRepository(props);

        resetIncrement(props, "Users");
        resetIncrement(props, "Excursions");
        resetIncrement(props, "Reservations");

        userRepository.save(new User("Ion", "1234"));
        Assert.assertEquals(userRepository.findAll().size(), 1);
        Assert.assertEquals(userRepository.findOne(1L).getUsername(), "Ion");
        userRepository.update(1L, new User("Mihai", "1234"));
        Assert.assertEquals(userRepository.findOne(1L).getUsername(), "Mihai");
        userRepository.delete(1L);
        Assert.assertEquals(userRepository.findAll().size(), 0);

        excursionRepository.save(new Excursion("muzeu","Tarom", 25, LocalTime.of(20,30,0), 50L));
        Assert.assertEquals(excursionRepository.findAll().size(), 1);
        Assert.assertEquals(excursionRepository.filterByObjective("muzeu", LocalTime.of(19,30,0),LocalTime.of(21,30,0)).size(), 1);
        Assert.assertEquals(excursionRepository.findOne(1L).getObjective(), "muzeu");
        excursionRepository.update(1L,new Excursion("opera","Tarom", 25, LocalTime.of(20,30,0), 50L));
        Assert.assertEquals(excursionRepository.findOne(1L).getObjective(), "opera");
        Assert.assertEquals(excursionRepository.filterByObjective("muzeu", LocalTime.of(19,30,0),LocalTime.of(21,30,0)).size(), 0);


        reservationRepository.save(new Reservation("John", 123456789L, 2L, 1L));
        Assert.assertEquals(reservationRepository.findAll().size(), 1);
        Assert.assertEquals(reservationRepository.findOne(1L).getName(), "John");
        reservationRepository.update(1L, new Reservation("Sam", 123456789L, 2L, 1L));
        Assert.assertEquals(reservationRepository.findOne(1L).getName(), "Sam");

        reservationRepository.delete(1L);
        Assert.assertEquals(reservationRepository.findAll().size(), 0);
        excursionRepository.delete(1L);
        Assert.assertEquals(excursionRepository.findAll().size(), 0);






    }
    public void resetIncrement(Properties props, String tableName){
        JdbcUtils dbUtils = new JdbcUtils(props);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("UPDATE `sqlite_sequence` SET `seq` = 0 WHERE `name` = ?;")) {
            preparedStatement.setString(1, tableName);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        } ;
    }
}
