using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CSharp.domain;

namespace CSharp.repository
{
    public interface IRepository<ID, E> where E : Entity<ID>
    {
        IEnumerable<E> FindAll();

        void Save(E entity);

        E FindOne(ID id);

        void Delete(ID id);

        void Update(ID id, E entity);
    }
}