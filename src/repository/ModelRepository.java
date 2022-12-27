package repository;

import java.util.List;

import enums.EmployeeDesignation;
import models.Client;
import models.Department;
import models.Employee;
import models.Feedback;
import models.Product;
import models.Question;
import models.Report;
import models.Ticket;
import models.Update;

public interface ModelRepository {

    public List<Department> getDepartments();

    public Department getDepartment(int departmentId);

    public Department addDepartment(String departmentName, Product product, int managerId);

    public boolean updateDepartment(Department department);

    public Department removeDepartment(Department department);

    public List<Employee> getEmployees();

    public Employee getEmployee(int employeeId);

    public Employee addEmployee(String username, String password, EmployeeDesignation designation, int departmentId,
            int mentorId);

    public boolean updateEmployee(Employee employee);

    public Employee removeEmployee(Employee employee);

    public List<Report> getReports();

    public Report addReport(Report report);

    public boolean updateReport(Report report);

    public Report removeReport(Report report);

    public List<Product> getProducts();

    public Product getProduct(String productName);

    public Product addProduct(Product product);

    public boolean updateProduct(Product product);

    public Product removeProduct(Product product);

    public List<Client> getClients();

    public Client getClient(String email);

    public Client addClient(Client client);

    public boolean updateClient(Client client);

    public Client removeClient(Client client);

    public List<Feedback> getFeedbacks();

    public Feedback addFeedback(Feedback feedback);

    public boolean updateFeedback(Feedback feedback);

    public Feedback removeFeedback(Feedback feedback);

    public List<Ticket> getTickets();

    public Ticket addTicket(Ticket ticket);

    public boolean updateTicket(Ticket ticket);

    public Ticket removeTicket(Ticket ticket);

    public List<Question> getQuestions();

    public List<Update> getUpdates();

    public Update addUpdate(Update update);

    public Update removeUpdate(Update update);

}
