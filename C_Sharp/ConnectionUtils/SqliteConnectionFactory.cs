using System;
using System.Data;

namespace ConnectionUtils
{
    public class SqliteConnectionFactory : ConnectionFactory
    {
        public override IDbConnection createConnection()
        {
            var connectionString = ConfigurationManager.AppSettings.Get("jdbc.url");
            return new SqliteConnection(connectionString);

            // Windows Sqlite Connection, fisierul .db ar trebuie sa fie in directorul debug/bin
            //String connectionString = "Data Source=tasks.db;Version=3";
            //return new SqliteConnection(connectionString);
        }
    }
    
}