using C_Sharp.domain;

namespace C_Sharp.Repository
{
    public interface IUserRepository : IRepository<long, User>
    {
        void Login(string username, string password);
    }
}