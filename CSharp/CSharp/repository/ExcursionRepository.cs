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
    public class ExcursionDBRepository : IExcursionRepository
    {

        private static readonly ILog log = LogManager.GetLogger("ExcursionDBRepository");
        private readonly IValidator<Excursion> excursionValidator;

        public ExcursionDBRepository( IValidator<Excursion> validator)
        {
            log.Info("Creating ExcursionDBRepository");
            this.excursionValidator = validator;
        }

        public IEnumerable<Excursion> FindAll()
        {
            IDbConnection con = JdbcManager.GetConnection();
            IList<Excursion> excursions = new List<Excursion>();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, company, price, start_time, seats, objective from Excursions";


                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string company = dataReader.GetString(1);
                        float price = dataReader.GetFloat(2);
                        TimeSpan time = DateTime.ParseExact(dataReader.GetString(3), "HH:mm:ss", System.Globalization.CultureInfo.CurrentCulture).TimeOfDay;
                        long seats = dataReader.GetInt64(4);
                        string objective = dataReader.GetString(5);
                        Excursion excursion = new Excursion(company, price, time, seats, objective);
                        excursion.Id = id;
                        excursions.Add(excursion);
                    }
                }
            }
            log.Info(excursions);
            return excursions;
        }

        public void Save(Excursion entity)
        {
            try
            {
                excursionValidator.validate(entity);
            }
            catch(ValidationException ex)
            {
                throw new RepositoryException(ex.Message);
            }
            log.InfoFormat("Entered save with entity {0}", entity);
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText =
                    "insert into Excursions(company, price, start_time, seats, objective) values (@company, @price, @start_time, @seats, @objective)";
                var paramCompany = command.CreateParameter();
                paramCompany.ParameterName = "@company";
                paramCompany.Value = entity.Company;
                command.Parameters.Add(paramCompany);

                var paramPrice = command.CreateParameter();
                paramPrice.ParameterName = "@price";
                paramPrice.Value = entity.Price;
                command.Parameters.Add(paramPrice);

                var paramTime = command.CreateParameter();
                paramTime.ParameterName = "@start_time";
                paramTime.Value = entity.Time;
                command.Parameters.Add(paramTime);

                var paramSeats = command.CreateParameter();
                paramSeats.ParameterName = "@seats";
                paramSeats.Value = entity.Seats;
                command.Parameters.Add(paramSeats);

                var paramObjective = command.CreateParameter();
                paramObjective.ParameterName = "@objective";
                paramObjective.Value = entity.Objective;
                command.Parameters.Add(paramObjective);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    Console.WriteLine("No excursion added!");
            }
        }
        
        public Excursion FindOne(long id)
        {
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText =
                    "select id, company, price, start_time, seats, objective from Excursions where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long idE = dataReader.GetInt64(0);
                        string company = dataReader.GetString(1);
                        float price = dataReader.GetFloat(2);
                        TimeSpan time = DateTime.ParseExact(dataReader.GetString(3), "HH:mm:ss", System.Globalization.CultureInfo.CurrentCulture).TimeOfDay;
                        long seats = dataReader.GetInt64(4);
                        string objective = dataReader.GetString(5);
                        Excursion excursion = new Excursion(company, price, time, seats, objective);
                        excursion.Id = idE;
                        return excursion;
                    }
                }
            }

            return null;
        }

        public void Delete(long id)
        {
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText = "delete from Excursions where id = @id";
                var paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                var dataR = command.ExecuteNonQuery();
                if (dataR == 0)
                {
                    Console.WriteLine("No excursion deleted!");
                }
            }
        }

        public void Update(long id, Excursion entity)
        {
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText =
                    "update Excursions set company = @company, price = @price, start_time = @start_time, seats = @seats, objective = @objective where id = @id";
                var paramCompany = command.CreateParameter();
                paramCompany.ParameterName = "@company";
                paramCompany.Value = entity.Company;
                command.Parameters.Add(paramCompany);

                var paramPrice = command.CreateParameter();
                paramPrice.ParameterName = "@price";
                paramPrice.Value = entity.Price;
                command.Parameters.Add(paramPrice);

                var paramTime = command.CreateParameter();
                paramTime.ParameterName = "@start_time";
                paramTime.Value = entity.Time;
                command.Parameters.Add(paramTime);

                var paramSeats = command.CreateParameter();
                paramSeats.ParameterName = "@seats";
                paramSeats.Value = entity.Seats;
                command.Parameters.Add(paramSeats);

                var paramObjective = command.CreateParameter();
                paramObjective.ParameterName = "@objective";
                paramObjective.Value = entity.Objective;
                command.Parameters.Add(paramObjective);

                var paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                var dataR = command.ExecuteNonQuery();
                if (dataR == 0)
                {
                    Console.WriteLine("No excursion updated!");
                }
            }
        }

        public IEnumerable<Excursion> FilterByObjective(string objectiveSearch, TimeSpan from, TimeSpan to)
        {
            IDbConnection con = JdbcManager.GetConnection();
            IList<Excursion> excursions = new List<Excursion>();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, company, price, start_time, seats, objective from Excursions where objective = @objective and start_time between @from and @to";
                var paramObjective = command.CreateParameter();
                paramObjective.ParameterName = "@objective";
                paramObjective.Value = objectiveSearch;
                command.Parameters.Add(paramObjective);

                var paramFrom = command.CreateParameter();
                paramFrom.ParameterName = "@from";
                paramFrom.Value = from.ToString();
                command.Parameters.Add(paramFrom);

                var paramTo = command.CreateParameter();
                paramTo.ParameterName = "@to";
                paramTo.Value = to.ToString();
                command.Parameters.Add(paramTo);

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long id = dataReader.GetInt64(0);
                        string company = dataReader.GetString(1);
                        float price = dataReader.GetFloat(2);
                        TimeSpan time = DateTime.Parse(dataReader.GetString(3)).TimeOfDay;
                        long seats = dataReader.GetInt64(4);
                        string objective = dataReader.GetString(5);
                        Excursion excursion = new Excursion(company, price, time, seats, objective);
                        excursion.Id = id;
                        excursions.Add(excursion);
                    }
                }
            }

            return excursions;
        }

    }
}
