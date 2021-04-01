using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;

namespace CSharp.services
{
    public interface IServices
    {
        void login(string username, string password);

        List<Excursion> findAllExcursions();

        List<Excursion> findAllTripsFiltered(string objective, TimeSpan from, TimeSpan to);

        void addReservation(String name, long phone, long tickets, Excursion excursion);
    }
}