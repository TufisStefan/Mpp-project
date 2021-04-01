using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;
using CSharp.domain.validators;
using CSharp.Repository;
using log4net;

namespace CSharp.repository
{
    public class UserDBRepository : IUserRepository
    {
        private static readonly ILog log = LogManager.GetLogger("UserDBRepository");

        private readonly IValidator<User> userValidator;
        public UserDBRepository(IValidator<User> userValidator)
        {
            this.userValidator = userValidator;
            log.Info("Creating UserDBRepository");
        }

        public IEnumerable<User> FindAll()
        {
            IDbConnection con = JdbcManager.GetConnection();
            IList<User> users = new List<User>();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, username, password from Users";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long idUser = dataReader.GetInt64(0);
                        string username = dataReader.GetString(1);
                        string password = dataReader.GetString(2);
                        User user = new User(username, password);
                        user.Id = idUser;
                        users.Add(user);
                    }
                }
            }
            log.Info(users);
            return users;
        }

        public void Save(User entity)
        {
            log.InfoFormat("Entered save with entity {0}", entity);
            IDbConnection con = JdbcManager.GetConnection();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "insert into Users(username, password) values (@username, @password)";
                var paramUsername = command.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = entity.UserName;
                command.Parameters.Add(paramUsername);

                var paramPassword = command.CreateParameter();
                paramUsername.ParameterName = "@password";
                paramUsername.Value = entity.Password;
                command.Parameters.Add(paramPassword);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    Console.WriteLine("No user added!");
            }
        }


        public User FindOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);

            IDbConnection con = JdbcManager.GetConnection();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, username, password from Users where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long idUser = dataReader.GetInt64(0);
                        string username = dataReader.GetString(1);
                        string password = dataReader.GetString(2);
                        User user = new User(username, password);
                        user.Id = idUser;
                        log.InfoFormat("Exiting findOne with value {0}", user);
                        return user;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);

            return null;
        }

        public void Delete(long id)
        {
            log.InfoFormat("entering delete with id {0}", id);
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText = "delete from Users where id = @id";
                var paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                var dataR = command.ExecuteNonQuery();
                if (dataR == 0)
                {
                    Console.WriteLine("No user deleted!");
                }
            }
        }

        public void Update(long id, User entity)
        {
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText = "update Users set username = @username, password = @password where id = @id";
                var paramUsername = command.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = entity.UserName;
                command.Parameters.Add(paramUsername);

                var paramPassword = command.CreateParameter();
                paramUsername.ParameterName = "@password";
                paramUsername.Value = entity.Password;
                command.Parameters.Add(paramPassword);

                var paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;

                var dataR = command.ExecuteNonQuery();
                if (dataR == 0)
                {
                    Console.WriteLine("No user updated!");
                }
            }
        }

        public void Login(string username, string password)
        {
            log.InfoFormat("Entering Login with username {0}, password {1}", username, password);

            IDbConnection con = JdbcManager.GetConnection();

            using (var command = con.CreateCommand())
            {
                command.CommandText =
                    "select id, username, password from Users where username = @username and password = @password";
                IDbDataParameter paramUsername = command.CreateParameter();
                paramUsername.ParameterName = "@username";
                paramUsername.Value = username;
                command.Parameters.Add(paramUsername);

                command.CommandText =
                    "select id, username, password from Users where username = @username and password = @password";
                IDbDataParameter paramPassword = command.CreateParameter();
                paramPassword.ParameterName = "@password";
                paramPassword.Value = password;
                command.Parameters.Add(paramPassword);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        log.Info("Login successful");
                    }
                    else
                    {
                        log.Info("Invalid account information");
                        throw new RepositoryException("Invalid credentials");
                    }
                }
            }



        }
    }


}