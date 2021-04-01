using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;

namespace CSharp.repository
{
    public interface IExcursionRepository : IRepository<long, Excursion>
    {
        IEnumerable<Excursion> FilterByObjective(string objective, TimeSpan from, TimeSpan to);

    }
}