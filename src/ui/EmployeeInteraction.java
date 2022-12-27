package ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import enums.EmployeeDesignation;
import enums.MenuOption;
import enums.TicketStatus;
import helpers.Constants;
import helpers.Input;
import models.Department;
import models.Employee;
import models.Product;
import models.Report;
import models.Ticket;
import models.Update;
import repository.EmployeeRepository;
import repository.ProductRepository;

public final class EmployeeInteraction extends UserInteraction {
    private Employee employee;
    private EmployeeRepository employeeRepository;

    public EmployeeInteraction(
            Employee employee,
            EmployeeRepository employeeRepository,
            ProductRepository productRepository) {
        super(productRepository);
        this.employee = employee;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void onLogin() {
        System.out.println(Constants.clearScreen);
        System.out.println("Welcome " + employee.getUsername());
        List<Report> reports = employeeRepository.getReportsReceived(employee.getId()).stream()
                .filter((report) -> report.getReportedDate().after(employee.getLastLoginDate()))
                .collect(Collectors.toList());
        if (!reports.isEmpty()) {
            System.out.println("There are new reports arrived for you " + employee.getUsername() + ".");
            if (Input.getYesOrNoAsBoolean("Do you want to see those reports now? [y/N] >> ")) {
                ListView<Report> listViewHelper = new ListView<>(reports);
                listViewHelper.listItems((report) -> new String[] {
                        "From: " + employeeRepository.getEmployee(report.getFromEmployeeId()).getUsername() + " ("
                                + report.getFromEmployeeId() + ")",
                        "Title: " + report.getTitle(),
                        "Body: " + report.getBody(),
                        "Reported Date: " + report.getReportedDate()
                });
                Input.waitUntilReturn();
            }
        }
        if (employee.getDesignation().equals(EmployeeDesignation.ADMIN))
            return;
        List<Ticket> tickets = productRepository.getTickets(
                employeeRepository.getDepartment(employee.getDepartmentId()).getProduct()).stream()
                .filter((ticket) -> ticket.getPublishedDate().after(employee.getLastLoginDate()))
                .collect(Collectors.toList());
        if (!tickets.isEmpty()) {
            System.out.println(Constants.clearScreen);
            System.out.println(
                    "while you were away there has been new helpdesk tickets raised for the product you working on!");
            if (Input.getYesOrNoAsBoolean("Do you want to see those raised helpdesk tickets? [y/N] >> ")) {
                ListView<Ticket> listViewHelper = new ListView<>(tickets);
                listViewHelper.listItems();
                Input.waitUntilReturn();
            }
        }
    }

    private void viewProfile() {
        System.out.println("Your profile:-");
        System.out.println();
        System.out.println();
        System.out.println("Employee Id   : " + employee.getId());
        System.out.println();
        System.out.println("Name          : " + employee.getUsername());
        System.out.println();
        System.out.println("Email         : " + employee.getEmail());
        System.out.println();
        System.out.println("Designation   : " + employee.getDesignation());
        System.out.println();
        if (!employee.getDesignation().equals(EmployeeDesignation.MANAGER)
                && !employee.getDesignation().equals(EmployeeDesignation.ADMIN)) {
            System.out.println("Mentor: " + employeeRepository.getEmployee(employee.getMentorId()).getUsername()
                    + " (id: " + employee.getMentorId() + ")");
            System.out.println();
        }
        if (!employee.getDesignation().equals(EmployeeDesignation.ADMIN)) {
            Department department = employeeRepository.getDepartment(employee.getDepartmentId());
            System.out.println("Department    : " + department.getName() + " (id: " + department.getId() + ")");
            System.out.println();
            System.out.println("Product       : " + department.getProduct().getName());
            System.out.println();
        }
        System.out.println("Last login    : " + employee.getLastLoginDate());
        System.out.println();
    }

    @Override
    public void afterLogin() {
        DialogMenu<MenuOption> dialog = new DialogMenu<>(getOptionsBasedOnDesignation()),
                editDialog = new DialogMenu<>(new MenuOption[] {
                        MenuOption.CHANGE_USERNAME,
                        MenuOption.CHANGE_PASSWORD,
                        MenuOption.GO_BACK });
        loop: do {
            System.out.println(Constants.clearScreen);
            System.out.println(dialog);
            MenuOption option = dialog.getOption();
            System.out.println(Constants.clearScreen);
            switch (option) {
                case VIEW_PROFILE:
                    viewProfile();
                    break;
                case EDIT_PROFILE:
                    System.out.println(editDialog);
                    switch (editDialog.getOption()) {
                        case CHANGE_USERNAME:
                            if (editUsername(employee) && employeeRepository.updateEmployee(employee))
                                System.out.println("Username changed successfully!");
                            break;
                        case CHANGE_PASSWORD:
                            if (editPassword(employee) && employeeRepository.updateEmployee(employee))
                                System.out.println("Password changed successfully!");
                            break;
                        case GO_BACK:
                        default:
                            break;
                    }
                    break;
                case VIEW_ALL_DEPARTMENTS:
                    viewAllDepartments();
                    break;
                case ADD_EMPLOYEE:
                    addEmployee();
                    break;
                case ADD_PRODUCT:
                case ADD_DEPARTMENT:
                    addProductOrDepartment();
                    break;
                case REMOVE_DEPARTMENT:
                case REMOVE_PRODUCT:
                    removeProductOrDepartment();
                    break;
                case RELEASE_AN_UPDATE:
                    releaseAnUpdate();
                    break;
                case VIEW_REPORTS_RECEIVED:
                    viewReports(false);
                    break;
                case VIEW_REPORTS_SENT:
                    viewReports(true);
                    break;
                case SEND_REPORT:
                    sendReport();
                    break;
                case VIEW_HELPDESK_TICKETS:
                    viewTickets();
                    break;
                case EAGLE_VIEW_DEPARTMENT:
                    eagleViewDepartment(employeeRepository.getDepartment(employee.getDepartmentId()));
                    break;
                case LOGOUT:
                default:
                    break loop;
            }
            Input.waitUntilReturn();
        } while (true);

    }

    private void viewAllDepartments() {
        ListView<Department> departments = new ListView<>(employeeRepository.getDepartments());
        departments.listItems();
        Department department = departments.selectItemByUser(
                "Enter the serial no. of the department listed above to view all employees in it >> ");
        if (department != null) {
            eagleViewDepartment(department);
            Input.waitUntilReturn();
        } else System.out.println("technical issue!");
    }

    public void releaseAnUpdate() {
        Product product = employeeRepository.getDepartment(employee.getDepartmentId()).getProduct();
        System.out.println("Oh, our clients will be very happy to see updates on our product " + product.getName());
        String whatsNew = Input.getString("Enter Whats' new about this update on this product >> ");
        if (Input.getYesOrNoAsBoolean("Do you want to publish this update [y/N] >> ")) {
            if (employeeRepository.releaseAnUpdate(new Update(whatsNew, product)) != null) {
                System.out.println("That's great, you have succesfully published an update!");
                Input.waitUntilReturn();
            }
        }
    }

    private void eagleViewDepartment(Department department) {
        System.out.println("Department : " + department.getName());
        System.out.println("Product    : " + department.getProduct().getName());
        System.out.println("Employees  : ");
        ListView<Employee> listView = new ListView<>(employeeRepository.getEmployees().stream()
                .filter((emp) -> emp.getDepartmentId() == employee.getDepartmentId()).collect(Collectors.toList()));
        listView.listItems((emp) -> {
            return new String[] {
                    "Name: " + emp.getUsername() + " (id: " + emp.getId() + ")",
                    "Designation: " + emp.getDesignation(),
                    "Mentor Id: " + emp.getMentorId()
            };
        });
        if (employee.getDesignation().equals(EmployeeDesignation.MANAGER)) {
            if (Input.getYesOrNoAsBoolean("Manager, Do you want to promote any one [Y/n] >> ")) {
                do {
                    Employee emp = listView.selectItemByUser(
                            "Select which one by entering the serial no. of the employee listed above >> ");
                    if (emp != null) {
                        EmployeeDesignation nextHigherDesignation = emp.getDesignation().getNextHigherDesignation();
                        if (nextHigherDesignation.rank() < EmployeeDesignation.MANAGER.rank()) {
                            if (Input.getYesOrNoAsBoolean(
                                    "Do you really want to promote this employee to " + nextHigherDesignation)) {
                                emp.setDesignation(nextHigherDesignation);
                                emp.setMentorId(employee.getId());
                                employeeRepository.updateEmployee(emp);
                            }
                        } else {
                            System.out.println("Oops, you have no power to promote this employee!");
                        }
                        break;
                    }
                } while (Input.getYesOrNoAsBoolean("Do you still want to promote anybody? [y/n] >> "));
            }
        }
    }

    private void addEmployee() {
        System.out.println();
        String name = Input.getUsername("ENTER YOUR NAME (with initial at last) >> ");
        System.out.println();
        String password = Input.getPasswordAfterConformPassword();
        System.out.println();
        System.out.println("A new email related to this corp will be generated!");
        System.out.println();
        ListView<Department> departments = new ListView<>(employeeRepository.getDepartments());
        departments.listItems();
        Department department = departments
                .selectItemByUser("Enter the serial no. of the department to add this employee >> ");
        ListView<Employee> employees = new ListView<>(employeeRepository.getEmployees().stream()
                .filter((emp) -> emp.getDepartmentId() == department.getId()).collect(Collectors.toList()));
        employees.listItems();
        // Employee employee = employees.selectItemByUser(
        // "Enter the serial no. of the employee from the list list above to make them
        // as the mentor of new employee >> ");
        // System.out.println();
        System.out.println("Finally choose the designation of this employee >> ");
        DialogMenu<EmployeeDesignation> dialog = new DialogMenu<>(EmployeeDesignation.values());
        System.out.println(dialog);
        EmployeeDesignation designation = dialog.getOption();
        Employee employee = employeeRepository.addEmployee(name, password, designation, department.getId(),
                employeeRepository.getMentorIdForNewEmployee(department));
        if (employee != null) {
            System.out.println();
            System.out.println("Here are the new employee credentials to note down!");
            System.out.println();
            System.out.println("Employee Id : " + employee.getId());
            System.out.println("Name        : " + employee.getUsername());
            System.out.println("Email       : " + employee.getEmail());
            System.out.println("Department  : " + department.getName());
            System.out.println("Designation : " + employee.getDesignation());
            System.out.println("Mentor      : " + employeeRepository.getEmployee(employee.getMentorId()).getUsername()
                    + "(id: " + employee.getMentorId() + ")");
            System.out.println("Please note down these information to help yourself on login!");
        } else
            System.out.println("Sorry, there is some technical issue on adding this new employee!");
    }

    private void addProductOrDepartment() {
        String productName = Input.getString("Enter new product name >> ");
        String productInfo = Input.getString("Enter product info >> ");
        String departmentName = Input.getString("Enter new department name >> ");
        List<Employee> employees = employeeRepository.getEmployees().stream()
                .filter((employee) -> !employee.getDesignation().equals(EmployeeDesignation.ADMIN)
                        && !employee.getDesignation().equals(EmployeeDesignation.MANAGER))
                .collect(Collectors.toList());
        if (employees.isEmpty()) {
            System.out.println(
                    "Sorry, but there are no employees available to appoint them as manager to this new product!");
            return;
        }
        ListView<Employee> listView = new ListView<>(employees);
        listView.listItems(employee -> {
            Department department = employeeRepository.getDepartment(employee.getDepartmentId());
            return new String[] {
                    "Name: " + employee.getUsername() + " (id: " + employee.getId() + ")",
                    "Designation: " + employee.getDesignation(),
                    "Department: " + department.getName(),
                    "Product: " + department.getProduct(),
            };
        });
        Employee manager = listView.selectItemByUser(
                "Enter the serial no. of the employee to make him as the manager of this product >> ");
        if (manager == null)
            return;
        Product product = new Product(productName, productInfo);
        if (productRepository.addProduct(product) != null) {
            Department department = employeeRepository.addDepartment(departmentName, product, manager.getId());
            if (department != null) {
                manager.setDepartmentId(department.getId());
                manager.setDesignation(EmployeeDesignation.MANAGER);
                manager.setMentorId(0);
                employeeRepository.updateEmployee(manager);
                System.out.println("Department and product has been successfully created!");
            }
        }
    }

    private void removeProductOrDepartment() {
        // TODO remove product or department
    }

    private void viewReports(boolean sent) {
        List<Report> reports = sent ? employeeRepository.getReportsSent(employee.getId())
                : employeeRepository.getReportsReceived(employee.getId());
        if (reports.isEmpty()) {
            System.out.println("Hah, NOoo reports " + (sent ? "sent" : "received") + " by you!");
            return;
        }
        ListView<Report> listView = new ListView<>(reports);
        listView.listItems((report) -> new String[] {
                (sent ? "To: " : "From: ")
                        + employeeRepository.getEmployee(sent ? report.getToEmployeeId()
                                : report.getFromEmployeeId()).getUsername()
                        + " (" + report.getFromEmployeeId() + ")",
                "Title: " + report.getTitle(),
                "Body: " + report.getBody(),
                "Reported Date: " + report.getReportedDate()
        });
    }

    private void sendReport() {
        List<Employee> employees = employeeRepository.getEmployees().stream()
                .filter((emp) -> emp.getMentorId() == employee.getId() || emp.getId() == employee.getMentorId())
                .collect(Collectors.toList());
        if (employees.isEmpty()) {
            System.out.println("sorry, there are no one now for you to send a email!");
            return;
        }
        ListView<Employee> listViewHelper = new ListView<>(employees);
        listViewHelper.listItems();
        Employee emp = listViewHelper
                .selectItemByUser("Enter serial no. (not id) of the employee to send report from list above >> ");
        String title = Input.getString("Enter title of the report >> ");
        String body = Input.getString("Enter body of the report >> ");
        Report report = employeeRepository.addReport(new Report(employee.getId(), emp.getId(), title, body));
        if (report != null) {
            System.out.println("Report sent successfully!");
        }
    }

    private void viewTickets() {
        List<Ticket> tickets = productRepository.getTickets(
                employeeRepository.getDepartment(employee.getDepartmentId()).getProduct());
        ListView<Ticket> listView = new ListView<>(tickets);
        listView.listItems();
        if (employee.getDesignation().equals(EmployeeDesignation.MANAGER)) {
            if (Input.getYesOrNoAsBoolean("Do you want to change any ticket status [y/N] >> ")) {
                Ticket ticket = listView.selectItemByUser("Give the serial no. of the ticket listed above >> ");
                System.out.println();
                System.out.println("Choose the ticket status type to apply:");
                System.out.println();
                DialogMenu<TicketStatus> dialog = new DialogMenu<>(TicketStatus.values());
                System.out.println(dialog);
                ticket.setStatus(dialog.getOption());
                if (productRepository.updateTicket(ticket)) {
                    System.out.println("Ticket updated Successfully!");
                    Input.waitUntilReturn();
                }
            }
        }
    }

    @Override
    public void onLogout() {
        if (employee != null) {
            employee.setLastLoginDate(new Date());
            employeeRepository.updateEmployee(employee);
        }
    }

    private MenuOption[] getOptionsBasedOnDesignation() {
        List<MenuOption> options = new LinkedList<>();
        options.add(MenuOption.VIEW_PROFILE);
        options.add(MenuOption.EDIT_PROFILE);
        options.add(MenuOption.VIEW_ALL_DEPARTMENTS);
        if (employee.getDesignation().equals(EmployeeDesignation.ADMIN)) {
            options.add(MenuOption.ADD_EMPLOYEE);
            options.add(MenuOption.ADD_PRODUCT);
            options.add(MenuOption.ADD_DEPARTMENT);
            options.add(MenuOption.REMOVE_DEPARTMENT);
            options.add(MenuOption.REMOVE_PRODUCT);
        } else {
            if (employee.getDesignation().equals(EmployeeDesignation.MANAGER)) {
                options.add(MenuOption.RELEASE_AN_UPDATE);
            }
            options.add(MenuOption.VIEW_REPORTS_RECEIVED);
            options.add(MenuOption.VIEW_REPORTS_SENT);
            options.add(MenuOption.SEND_REPORT);
            options.add(MenuOption.VIEW_HELPDESK_TICKETS);
            options.add(MenuOption.EAGLE_VIEW_DEPARTMENT);
        }
        options.add(MenuOption.LOGOUT);
        MenuOption[] opts = new MenuOption[options.size()];
        int i = 0;
        for (MenuOption option : options)
            opts[i++] = option;
        return opts;
    }
}
