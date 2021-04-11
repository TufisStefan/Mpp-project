package repository;

import domain.User;
import domain.validators.ValidationException;
import domain.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private static Validator<User> validator;

    public UserDBRepository(Properties props, Validator<User> validator) {
        logger.info("Initializing UserDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        UserDBRepository.validator = validator;
    }

    @Override
    public User login(String username, String password) throws RepositoryException {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Users where username = ? and password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    logger.traceExit();
                    return new User(username, password);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println(ex);
        }
        logger.traceExit("Username and password combination not valid");
        throw new RepositoryException("Invalid username or password!");
    }

    @Override
    public User findOne(Long aLong) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Users where id=?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println(ex);
        }
        logger.traceExit("No user with id {} found", aLong);
        return null;
    }

    @Override
    public List<User> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = con.prepareStatement("select * from Users")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = (long) result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit(users);
        return users;

    }

    @Override
    public void save(User entity) throws ValidationException {
        logger.traceEntry("Saving user {}", entity);
        try{
            validator.validate(entity);
        }
        catch (ValidationException e) {
            throw e;
        }

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("insert into Users(username,password) values (?,?)")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            int result = preparedStatement.executeUpdate();
            logger.trace("Saved {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Invalid username\n");
        }

    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Users where id=?")) {
            preStmt.setLong(1, aLong);
            int result = preStmt.executeUpdate();
            logger.trace("Deleted {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void update(Long aLong, User entity) throws ValidationException {
        logger.traceEntry("update user {}", entity);

        try{
            validator.validate(entity);
        }
        catch (ValidationException e) {
            throw e;
        }

        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("update Users set username = ?, password = ? where id = ?;")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setLong(3, aLong);
            int result = preparedStatement.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        logger.traceExit();
    }
}
