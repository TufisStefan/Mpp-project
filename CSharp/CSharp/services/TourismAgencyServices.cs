using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;
using CSharp.domain.validators;
using CSharp.repository;
using CSharp.Repository;

namespace CSharp.services
{
    public class TourismAgencyServices : IServices
    {
        private readonly IUserRepository userRepository;
        private readonly IExcursionRepository excursionRepository;
        private readonly IReservationRepository reservationRepository;

        public TourismAgencyServices(IUserRepository userRepository, IExcursionRepository excursionRepository, IReservationRepository reservationRepository)
        {
            this.userRepository = userRepository;
            this.excursionRepository = excursionRepository;
            this.reservationRepository = reservationRepository;
        }

        public void login(string username, string password)
        {
            try
            {
                userRepository.Login(username, password);
            }
            catch (RepositoryException ex)
            {
                throw new ServicesException(ex.Message);
            }
        }

        public List<Excursion> findAllExcursions()
        {
            return excursionRepository.FindAll().ToList();
        }

        public List<Excursion> findAllTripsFiltered(string objective, TimeSpan from, TimeSpan to)
        {
            return excursionRepository.FilterByObjective(objective, from, to).ToList();
        }

        public void addReservation(String name, long phone, long tickets, Excursion excursion)
        {
            long id = excursion.Id;
            Excursion repoExcursion = excursionRepository.FindOne(id);
            if (repoExcursion == null)
            {
                throw new ServicesException("Excursion not found!");
            }
            if (repoExcursion.Seats < tickets)
            {
                throw new ServicesException("Not enough available seats!");
            }
            Reservation reservation = new Reservation(name, phone, tickets, id);
            try
            {
                reservationRepository.Save(reservation);
                repoExcursion.Seats = repoExcursion.Seats - tickets;
                excursionRepository.Update(id, repoExcursion);
            }
            catch (RepositoryException ex)
            {
                throw new ServicesException(ex.Message);
            }
        }
    }
}