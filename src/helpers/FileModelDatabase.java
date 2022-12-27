package helpers;

import models.Client;
import models.Department;
import models.Employee;
import models.Feedback;
import models.Product;
import models.Report;
import models.Ticket;
import models.Update;

public final class FileModelDatabase implements FileModelHelper {
    private String delim = "|";
    private String regex = "\\|";

    @Override
    public String modelToString(Department department) {
        return department.getId() + delim
                + department.getName() + delim
                + department.getProduct().getName() + delim
                + department.getManagerId();
    }

    @Override
    public String modelToString(Employee employee) {
        return employee.getId() + delim
                + employee.getUsername() + delim
                + employee.getEmail() + delim
                + employee.getPassword() + delim
                + employee.getDesignation().name() + delim
                + employee.getDepartmentId() + delim
                + employee.getMentorId() + delim
                + employee.getLastLoginDate();
    }

    @Override
    public String modelToString(Client client) {
        String string = client.getUsername() + delim
                + client.getEmail() + delim
                + client.getPassword() + delim
                + client.getLastLoginDate() + delim;
        for (Product product : client.getSubscribedProducts())
            string += product.getName() + ",";
        return string;
    }

    @Override
    public String modelToString(Feedback feedback) {
        return feedback.getProduct().getName() + delim
                + feedback.getClient().getEmail() + delim
                + feedback.getUpvotes() + delim
                + feedback.getPublishedDate() + delim
                + feedback.getMessage();
    }

    @Override
    public String modelToString(Product product) {
        return product.getName() + delim + product.getInfo();
    }

    @Override
    public String modelToString(Report report) {
        return report.getFromEmployeeId() + delim
                + report.getToEmployeeId() + delim
                + report.getTitle() + delim
                + report.getBody() + delim
                + report.getReportedDate();
    }

    @Override
    public String modelToString(Ticket ticket) {
        return ticket.getProduct().getName() + delim
                + ticket.getClient().getEmail() + delim
                + ticket.getStatus().name() + delim
                + ticket.getUpvotes() + delim
                + ticket.getPublishedDate() + delim
                + ticket.getMessage();
    }

    @Override
    public String modelToString(Update update) {
        return update.getProduct().getName() + delim
                + update.getPostedDate() + delim
                + update.getWhatsNew();
    }

    @Override
    public String[] stringToDepartment(String string) {
        return string.split(regex);
    }

    @Override
    public String[] stringToEmployee(String string) {
        return string.split(regex);
    }

    @Override
    public String[] stringToClient(String string) {
        return string.split(regex);
    }

    @Override
    public String[] stringToFeedback(String string) {
        return getInCorrectOrder(5, string);
    }

    @Override
    public String[] stringToProduct(String string) {
        return string.split(regex, 2);
    }

    @Override
    public String[] stringToReport(String string) {
        String[] strings = new String[5];
        for (int i = 0; i < strings.length; i++) {
            int delimIdx = i == strings.length - 2 ? string.lastIndexOf(delim) : string.indexOf(delim);
            strings[i] = delimIdx > -1 ? string.substring(0, delimIdx) : string;
            string = string.substring(delimIdx + 1);
        }
        return strings;
    }

    @Override
    public String[] stringToTicket(String string) {
        return getInCorrectOrder(6, string);
    }

    @Override
    public String[] stringToUpdate(String string) {
        return getInCorrectOrder(3, string);
    }

    private String[] getInCorrectOrder(int limit, String string) {
        String[] strings = string.split(regex, limit);
        String temp = strings[strings.length - 1];
        for (int i = strings.length - 1; i > 0; i--)
            strings[i] = strings[i - 1];
        strings[0] = temp;
        return strings;
    }

    @Override
    public boolean isPrimaryKeyEqual(Department department, String data) {
        return Integer.parseInt(data.substring(0, data.indexOf(delim))) == department.getId();
    }

    @Override
    public boolean isPrimaryKeyEqual(Employee employee, String data) {
        return Integer.parseInt(data.substring(0, data.indexOf(delim))) == employee.getId();
    }

    @Override
    public boolean isPrimaryKeyEqual(Client client, String data) {
        return stringToClient(data)[1].equals(client.getEmail());
    }

    @Override
    public boolean isPrimaryKeyEqual(Report report, String data) {
        return Constants.dateParse(stringToReport(data)[4]).equals(report.getReportedDate());
    }

    @Override
    public boolean isPrimaryKeyEqual(Product product, String data) {
        return data.substring(0, data.indexOf(delim)).equals(product.getName());
    }

    @Override
    public boolean isPrimaryKeyEqual(Feedback feedback, String data) {
        return Constants.dateParse(stringToFeedback(data)[4]).equals(feedback.getPublishedDate());
    }

    @Override
    public boolean isPrimaryKeyEqual(Ticket ticket, String data) {
        return Constants.dateParse(stringToTicket(data)[5]).equals(ticket.getPublishedDate());
    }

    @Override
    public boolean isPrimaryKeyEqual(Update update, String data) {
        return Constants.dateParse(stringToUpdate(data)[2]).equals(update.getPostedDate());
    }

}
