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
    public class ReservationDBRepository : IReservationRepository
    {


        private static readonly ILog log = LogManager.GetLogger("ReservationDBRepository");
        private readonly IValidator<Reservation> reservationValidator;

        public ReservationDBRepository(IValidator<Reservation> validator)
        {
            log.Info("Creating ReservationDBRepository");
            this.reservationValidator = validator;
        }
        public IEnumerable<Reservation> FindAll()
        {
            IDbConnection con = JdbcManager.GetConnection();
            IList<Reservation> reservations = new List<Reservation>();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, name, phone, tickets, excursionId from Reservations";

                using (var dataReader = command.ExecuteReader())
                {
                    while (dataReader.Read())
                    {
                        long idRes = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        long phone = dataReader.GetInt64(2);
                        long tickets = dataReader.GetInt64(3);
                        long excursionId = dataReader.GetInt64(4);

                        Reservation reservation = new Reservation(name, phone, tickets, excursionId);
                        reservation.Id = idRes;
                        reservations.Add(reservation);
                    }
                }
            }
            log.Info(reservations);
            return reservations;
        }

        public void Save(Reservation entity)
        {
            log.InfoFormat("Entered save with entity {0}", entity);
            IDbConnection con = JdbcManager.GetConnection();
            try
            {
                reservationValidator.validate(entity);
            }
            catch(ValidationException ex)
            {
                throw new RepositoryException(ex.Message);
            }
            using (var command = con.CreateCommand())
            {
                command.CommandText = "insert into Reservations(name, phone, tickets, excursionId) values (@name, @phone, @tickets, @excursionId)";
                var paramName = command.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                command.Parameters.Add(paramName);

                var paramPhone = command.CreateParameter();
                paramPhone.ParameterName = "@phone";
                paramPhone.Value = entity.PhoneNumber;
                command.Parameters.Add(paramPhone);

                var paramTickets = command.CreateParameter();
                paramTickets.ParameterName = "@tickets";
                paramTickets.Value = entity.TicketsNumber;
                command.Parameters.Add(paramTickets);

                var paramId = command.CreateParameter();
                paramId.ParameterName = "@excursionId";
                paramId.Value = entity.TicketsNumber;
                command.Parameters.Add(paramId);

                var result = command.ExecuteNonQuery();
                if (result == 0)
                    Console.WriteLine("No reservation added!");
            }
        }

        public Reservation FindOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);

            IDbConnection con = JdbcManager.GetConnection();

            using (var command = con.CreateCommand())
            {
                command.CommandText = "select id, name, phone, tickets, excursionId from Reservations where id = @id";
                IDbDataParameter paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                using (var dataReader = command.ExecuteReader())
                {
                    if (dataReader.Read())
                    {
                        long idRes = dataReader.GetInt64(0);
                        string name = dataReader.GetString(1);
                        long phone = dataReader.GetInt64(2);
                        long tickets = dataReader.GetInt64(3);
                        long excursionId = dataReader.GetInt64(4);


                        Reservation reservation = new Reservation(name, phone, tickets, excursionId);
                        reservation.Id = idRes;

                        log.InfoFormat("Exiting findOne with value {0}", reservation);
                        return reservation;
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
                command.CommandText = "delete from Reservations where id = @id";
                var paramId = command.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                command.Parameters.Add(paramId);

                var dataR = command.ExecuteNonQuery();
                if (dataR == 0)
                {
                    Console.WriteLine("No reservation deleted!");
                }
            }
        }

        public void Update(long id, Reservation entity)
        {
            IDbConnection con = JdbcManager.GetConnection();
            using (var command = con.CreateCommand())
            {
                command.CommandText = "update Reservations set name = @name, phone = @phone, tickets = @tickets, excursionId = @excursionId where id = @id";
                var paramName = command.CreateParameter();
                paramName.ParameterName = "@name";
                paramName.Value = entity.Name;
                command.Parameters.Add(paramName);

                var paramPhone = command.CreateParameter();
                paramPhone.ParameterName = "@phone";
                paramPhone.Value = entity.PhoneNumber;
                command.Parameters.Add(paramPhone);

                var paramTickets = command.CreateParameter();
                paramTickets.ParameterName = "@tickets";
                paramTickets.Value = entity.TicketsNumber;
                command.Parameters.Add(paramTickets);

                var paramIdExcursion = command.CreateParameter();
                paramIdExcursion.ParameterName = "@excursionId";
                paramIdExcursion.Value = entity.TicketsNumber;
                command.Parameters.Add(paramIdExcursion);

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
    }
}