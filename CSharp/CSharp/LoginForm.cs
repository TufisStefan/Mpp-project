using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CSharp.domain;
using CSharp.domain.validators;
using CSharp.repository;
using CSharp.services;

namespace CSharp
{
    public partial class LoginForm : Form
    {
        IServices services;
        public LoginForm()
        {
            InitializeComponent();
        }

        private void LoginForm_Load(object sender, EventArgs e)
        {
            UserDBRepository userDBRepo = new UserDBRepository( new UserValidator());
            ExcursionDBRepository excursionDBRepo = new ExcursionDBRepository(new ExcursionValidator());
            ReservationDBRepository reservationDBRepo = new ReservationDBRepository(new ReservationValidator());
            services = new TourismAgencyServices(userDBRepo, excursionDBRepo, reservationDBRepo);

            passwordTextBox.PasswordChar = '*';
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string username = usernameTextBox.Text;
            string password = passwordTextBox.Text;
            try
            {
                services.login(username, password);
                new MainForm(services, this).Show();
                this.Hide();
            }
            catch(ServicesException ex)
            {
                MessageBox.Show(ex.Message, "Login Error");
            }

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

    }
}
