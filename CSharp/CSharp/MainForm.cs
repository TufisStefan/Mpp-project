using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CSharp.services;

namespace CSharp.domain
{
    public partial class MainForm : Form
    {
        IServices services;
        Form home;

        public MainForm(IServices services, Form home)
        {
            this.services = services;
            this.home = home;
            InitializeComponent();
            excursionDataGrid.Columns.Add("id", "Id");
            excursionDataGrid.Columns["id"].Visible = false;
            excursionDataGrid.Columns.Add("objective", "Objective");
            excursionDataGrid.Columns.Add("company", "Company");
            excursionDataGrid.Columns.Add("time", "Departure time");
            excursionDataGrid.Columns.Add("price", "Price");
            excursionDataGrid.Columns.Add("seats", "Seats");
            updateTable(services.findAllExcursions());

            for (int i = 0; i < 24; i++)
            {
                for (int j = 0; j < 60; j += 15)
                {
                    String hour = i / 10 == 0 ? "0" + i : "" + i;
                    String minute = j / 10 == 0 ? "0" + j : "" + j;
                    startTimeComboBox.Items.Add(String.Format("{0}:{1}:00", hour, minute));
                    endTimeComboBox.Items.Add(String.Format("{0}:{1}:00", hour, minute));
                }
            }

            startTimeComboBox.SelectedIndex = 0;
            endTimeComboBox.SelectedIndex = endTimeComboBox.Items.Count - 1;
        }

        private void updateTable(List<Excursion> excursionsList)
        {
            excursionDataGrid.Rows.Clear();
            excursionsList.ForEach((x) => {
                excursionDataGrid.Rows.Add(x.Id, x.Objective, x.Company, x.Time, x.Price, x.Seats);
            });

            foreach (DataGridViewRow row in excursionDataGrid.Rows)
                if (Convert.ToInt32(row.Cells[5].Value) == 0)
                {
                    row.DefaultCellStyle.BackColor = Color.Red;
                }
            excursionDataGrid.Rows[excursionDataGrid.Rows.Count - 1].DefaultCellStyle.BackColor = Color.White;

        }

        public MainForm()
        {
            InitializeComponent();
        }

        private void MainForm_Load(object sender, EventArgs e)
        {

        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void logoutButton_Click(object sender, EventArgs e)
        {
            home.Show();
            this.Close();
        }

        private void filterButton_Click(object sender, EventArgs e)
        {
            string objective = objectiveTextBox.Text;
            TimeSpan from = DateTime.Parse(startTimeComboBox.Text).TimeOfDay;
            TimeSpan to = DateTime.Parse(endTimeComboBox.Text).TimeOfDay;
            updateTable(services.findAllTripsFiltered(objective, from, to));
        }

        private void startTimeComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {

        }

        private void reserveButton_Click(object sender, EventArgs e)
        {
            string name = nameTextBox.Text;
            long tickets = long.Parse(ticketsUpDown.Value.ToString());
            if (0 == excursionDataGrid.SelectedRows.Count)
            {
                MessageBox.Show("Select an excursion!","Error");
                return;
            }
            var row = excursionDataGrid.SelectedRows[0];
            Excursion excursion = new Excursion(row.Cells["company"].Value.ToString(), float.Parse(row.Cells["price"].Value.ToString()), DateTime.Parse(row.Cells["time"].Value.ToString()).TimeOfDay, long.Parse(row.Cells["seats"].Value.ToString()), row.Cells["objective"].Value.ToString());
            long id = long.Parse(excursionDataGrid.SelectedRows[0].Cells["id"].Value.ToString());
            excursion.Id = id;
            try
            {
                long phone = long.Parse(phoneTextBox.Text);
                services.addReservation(name, phone, tickets, excursion);
                updateTable(services.findAllExcursions());
                MessageBox.Show("Reservation completed!", "Success");
            }
            catch(ServicesException ex)
            {
                MessageBox.Show(ex.Message, "Error");
            }
            catch (FormatException ex)
            {
                MessageBox.Show("Insert a phone number!", "Error");
            }
        }
    }
}
