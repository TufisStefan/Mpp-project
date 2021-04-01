using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;

namespace CSharp.repository
{
    public interface IReservationRepository : IRepository<long, Reservation>
    {

    }
}