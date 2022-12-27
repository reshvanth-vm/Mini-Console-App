package repository;

import java.util.List;

import enums.EmployeeDesignation;
import models.Department;
import models.Employee;
import models.Product;
import models.Question;
import models.Report;
import models.Update;

public interface EmployeeRepository {

    public Department addDepartment(String departmentName, Product product, int managerId);

    public Department getDepartment(int departmentId);

    public List<Department> getDepartments();

    public Employee getEmployee(int employeeId);

    public Employee getEmployee(String email, String password);

    public List<Employee> getEmployees();

    public Employee addEmployee(String username, String password,
            EmployeeDesignation designation, int departmentId, int mentorId);
    
    public boolean updateEmployee(Employee employee);

    public Report addReport(Report report);

    public List<Report> getReports(int from, int to);

    public List<Report> getReportsSent(int from);

    public List<Report> getReportsReceived(int to);

    public Department getDepartmentOfLowestEmployeeCount();

    public int getMentorIdForNewEmployee(Department department);

    public Update releaseAnUpdate(Update update);

    public Question[] getQuestions();

}
