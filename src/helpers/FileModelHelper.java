package helpers;

import models.Client;
import models.Department;
import models.Employee;
import models.Feedback;
import models.Product;
import models.Report;
import models.Ticket;
import models.Update;

public interface FileModelHelper {

    public String modelToString(Department department);

    public String modelToString(Employee employee);

    public String modelToString(Client client);

    public String modelToString(Feedback feedback);

    public String modelToString(Product product);

    public String modelToString(Report report);

    public String modelToString(Ticket ticket);

    public String modelToString(Update update);

    public String[] stringToDepartment(String string);

    public String[] stringToEmployee(String string);

    public String[] stringToClient(String string);

    public String[] stringToFeedback(String string);

    public String[] stringToProduct(String string);

    public String[] stringToReport(String string);

    public String[] stringToTicket(String string);

    public String[] stringToUpdate(String update);

    public boolean isPrimaryKeyEqual(Department department, String data);

    public boolean isPrimaryKeyEqual(Employee employee, String data);

    public boolean isPrimaryKeyEqual(Client client, String data);

    public boolean isPrimaryKeyEqual(Report report, String data);

    public boolean isPrimaryKeyEqual(Product product, String data);

    public boolean isPrimaryKeyEqual(Feedback feedback, String data);

    public boolean isPrimaryKeyEqual(Ticket ticket, String data);

    public boolean isPrimaryKeyEqual(Update update, String data);

}
