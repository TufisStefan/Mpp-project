package repository;

import domain.Reservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReservationDBRepository implements ReservationRepository{

    private JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ReservationDBRepository(Properties props) {
        logger.info("Initializing ReservationDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Reservation findOne(Long aLong) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Reservations where id=?")){
            preparedStatement.setLong(1,aLong);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    String name = result.getString("name");
                    Long phone = result.getLong("phone");
                    Long tickets = result.getLong("tickets");
                    Long excursionId = result.getLong("excursionId");
                    Reservation reservation = new Reservation(name, phone, tickets, excursionId);
                    logger.traceExit(reservation);
                    return reservation;
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.out.println(ex);
        }
        logger.traceExit("No reservation with id {} found", aLong);
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Reservation> reservations = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Reservations")){
            try(ResultSet result = preparedStatement.executeQuery()) {
                while(result.next()) {
                    Long id = result.getLong("id");
                    String name = result.getString("name");
                    Long phone = result.getLong("phone");
                    Long tickets = result.getLong("tickets");
                    Long excursionId = result.getLong("excursionId");
                    Reservation reservation = new Reservation(name, phone, tickets, excursionId);
                    reservation.setId(id);
                    reservations.add(reservation);
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
            System.err.println("Error DB"+ex);
        }
        logger.traceExit(reservations);
        return reservations;
    }

    @Override
    public void save(Reservation entity) {
        logger.traceEntry("Saving reservation {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into Reservations(name, phone, tickets, excursionId) values (?,?,?,?)")){
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2,entity.getPhoneNumber());
            preparedStatement.setLong(3, entity.getTicketsNumber());
            preparedStatement.setLong(4, entity.getExcursionID());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }


    @Override
    public void delete(Long aLong) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Reservations where id=?")){
            preStmt.setLong(1,aLong);
            int result=preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Reservation entity) {
        logger.traceEntry("update reservation {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("update Reservations set name = ?, phone = ?, tickets = ?, excursionId = ? where id = ?;")){
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getPhoneNumber());
            preparedStatement.setLong(3, entity.getTicketsNumber());
            preparedStatement.setLong(4, entity.getExcursionID());
            preparedStatement.setLong(5,aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

}
