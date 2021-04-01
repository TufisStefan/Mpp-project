//using Microsoft.Data.Sqlite;
using Mono.Data.Sqlite;
using System;
using System.Configuration;
using System.Data;

namespace CSharp.repository
{
    public static class JdbcManager
    {
        private static SqliteConnection _connection;

        public static SqliteConnection GetConnection()
        {
            //string url = "URI=file:/Users/asus/Databases/tourism_agency.db,Version=3";
            var url = ConfigurationManager.AppSettings.Get("jdbc.url");
            try
            {
                _connection = new SqliteConnection(url);

                if (_connection.State == ConnectionState.Closed)
                {
                    _connection.Open();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
            return _connection;
        }
    }
}