package repository;


import domain.Excursion;
import domain.validators.ExcursionValidator;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExcursionDBRepository implements ExcursionRepository{

    private final JdbcUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static Validator<Excursion> validator;

    public ExcursionDBRepository(Properties props, Validator<Excursion> validator) {
        logger.info("Initializing ExcursionDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
        ExcursionDBRepository.validator = validator;
    }

    @Override
    public List<Excursion> filterByObjective(String objective, LocalTime from, LocalTime to) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Excursion> excursions = new ArrayList<>();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Excursions WHERE objective = ? and start_time between ? and ?")){
            preparedStatement.setString(1, objective);
            preparedStatement.setString(2, String.valueOf(from));
            preparedStatement.setString(3, String.valueOf(to));
            try(ResultSet result = preparedStatement.executeQuery()){
                while (result.next()) {
                    Long id = result.getLong("id");
                    String company = result.getString("company");
                    float price = result.getFloat("price");
                    LocalTime time = LocalTime.parse(result.getString("start_time"), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    Long seats = result.getLong("seats");
                    Excursion excursion = new Excursion(objective, company, price, time, seats);
                    excursion.setId(id);
                    excursions.add(excursion);
                }
            }
            return excursions;
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit("No excursion found");
        return null;
    }

    @Override
    public Excursion findOne(Long aLong) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Excursions where id=?")) {
            preparedStatement.setLong(1, aLong);
            try(ResultSet result = preparedStatement.executeQuery()){
                if(result.next()){
                    String company = result.getString("company");
                    float price = result.getFloat("price");
                    LocalTime time = LocalTime.parse(result.getString("start_time"), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    Long seats = result.getLong("seats");
                    String objective = result.getString("objective");
                    Excursion excursion = new Excursion(objective, company, price, time, seats);
                    logger.traceExit(excursion);
                    return excursion;
                }
            }

        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit("No excursion with id {} found", aLong);
        return null;
    }

    @Override
    public List<Excursion> findAll() {
        logger.traceEntry();
        List<Excursion> excursions = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("select * from Excursions")){
            try(ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("id");
                    String company = result.getString("company");
                    float price = result.getFloat("price");
                    LocalTime time = LocalTime.parse(result.getString("start_time"), DateTimeFormatter.ofPattern("HH:mm:ss"));
                    Long seats = result.getLong("seats");
                    String objective = result.getString("objective");
                    Excursion excursion = new Excursion(objective, company, price, time, seats);
                    excursion.setId(id);
                    excursions.add(excursion);
                }
            }
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit(excursions);
        return excursions;
    }

    @Override
    public void save(Excursion entity) throws ValidationException {
        logger.traceEntry("Saving excursion {}", entity);

        try{
            validator.validate(entity);
        }
        catch (ValidationException e) {
            throw e;
        }
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("insert into Excursions(company, price, start_time, seats, objective) values (?,?,time(?),?,?)")){
            preparedStatement.setString(1, entity.getCompany());
            preparedStatement.setFloat(2,entity.getPrice());
            preparedStatement.setString(3, String.valueOf(entity.getTime()));
            preparedStatement.setLong(4, entity.getSeats());
            preparedStatement.setString(5, entity.getObjective());
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
        logger.traceEntry("Deleting excursion with id {}", aLong);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("DELETE from Excursions WHERE id = ?")){
            preparedStatement.setLong(1, aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("Deleted {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Long aLong, Excursion entity) throws ValidationException {
        logger.traceEntry("update excursion {}", entity);

        try{
            validator.validate(entity);
        }
        catch (ValidationException e) {
            throw e;
        }

        Connection con = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = con.prepareStatement("update Excursions set company = ?, price = ?, start_time = time(?), seats = ?, objective = ? where id = ?;")){
            preparedStatement.setString(1, entity.getCompany());
            preparedStatement.setFloat(2, entity.getPrice());
            preparedStatement.setString(3, String.valueOf(entity.getTime()));
            preparedStatement.setLong(4, entity.getSeats());
            preparedStatement.setString(5,entity.getObjective());
            preparedStatement.setLong(6,aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }
}
