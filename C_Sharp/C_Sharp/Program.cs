using System;
using C_Sharp.Repository;

namespace C_Sharp
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            IUserRepository userRepository = new UserDBRepository();
            IExcursionRepository excursionRepository = new ExcursionDBRepository();
            IReservationRepository reservationRepository = new ReservationDBRepository();

            Console.WriteLine(excursionRepository.FindAll());
        }
    }
}