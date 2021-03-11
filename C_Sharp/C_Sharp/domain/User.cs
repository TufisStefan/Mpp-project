namespace C_Sharp.domain
{
    public class User : Entity<long>
    {
        public string UserName { get; set; }
        public string Password { get; set; }

        public User(string userName, string password)
        {
            UserName = userName;
            Password = password;
        }
    }
}