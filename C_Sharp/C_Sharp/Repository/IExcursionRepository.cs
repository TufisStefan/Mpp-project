
using System;
using System.Collections.Generic;
using C_Sharp.domain;

namespace C_Sharp.Repository
{
    public interface IExcursionRepository:IRepository<long, Excursion>
    {
        IEnumerable<Excursion> FilterByObjective(string objective, DateTime from, DateTime to);

    }
}