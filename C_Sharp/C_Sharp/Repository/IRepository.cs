using System.Collections.Generic;
using C_Sharp.domain;

namespace C_Sharp.Repository
{
    public interface IRepository <ID, E> where E : Entity<ID>
    {
        IEnumerable<E> FindAll();

        void Save(E entity);

        E FindOne(ID id);

        void Delete(ID id);

        void Update(ID id, E entity);
    }
}