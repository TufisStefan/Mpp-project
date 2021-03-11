using System.Collections.Generic;
using C_Sharp.domain;

namespace C_Sharp.Repository
{
    public interface IRepository <ID, E> where E : Entity<ID>
    {
        IEnumerable<E> FindAll();

        E Save(E entity);

        E FindOne(ID id);

        E Delete(ID id);

        E Update(E entity);
    }
}