package databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import enums.EmployeeDesignation;
import enums.TicketStatus;
import helpers.Constants;
import helpers.FileModelHelper;
import models.Client;
import models.Department;
import models.Employee;
import models.Feedback;
import models.Product;
import models.Question;
import models.Report;
import models.Ticket;
import models.Update;
import repository.ModelRepository;

public final class ModelDatabase implements ModelRepository {
    private static ModelDatabase modelDatabase;
    private FileModelHelper fileModelHelper;

    private int lastDepartmentId, lastEmployeeId;

    private List<Department> departments;
    private List<Employee> employees;
    private List<Report> reports;
    private List<Product> products;
    private List<Client> clients;
    private List<Feedback> feedbacks;
    private List<Ticket> tickets;
    private List<Update> updates;
    private List<Question> questions;

    private ModelDatabase(FileModelHelper fileModelConverter) {
        this.fileModelHelper = fileModelConverter;
        try (BufferedReader br = new BufferedReader(new FileReader(Constants.miscelleneousFilePath))) {
            lastDepartmentId = Integer.parseInt(br.readLine());
            lastEmployeeId = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModelDatabase getInstance(FileModelHelper fileModelConverter) {
        if (modelDatabase == null)
            modelDatabase = new ModelDatabase(fileModelConverter);
        return modelDatabase;
    }

    @Override
    public List<Department> getDepartments() {
        if (departments == null) {
            departments = new LinkedList<>();
            readDataFromFile(Constants.departmentsFilePath, (br, line) -> {
                String[] deptData = fileModelHelper.stringToDepartment(line);
                departments.add(new Department(
                        Integer.parseInt(deptData[0]),
                        deptData[1],
                        getProduct(deptData[2]),
                        Integer.parseInt(deptData[3])));
            });
        }
        return departments;
    }

    @Override
    public Department getDepartment(int departmentId) {
        for (Department department : getDepartments())
            if (department.getId() == departmentId)
                return department;
        return null;
    }

    @Override
    public Department addDepartment(String departmentName, Product product, int managerId) {
        Department department = new Department(lastDepartmentId + 1, departmentName, product, managerId);
        lastDepartmentId++;
        return appendDataInFile(Constants.departmentsFilePath, fileModelHelper.modelToString(department))
                && writeMiscellaneous()
                && departments.add(department) ? department : null;
    }

    @Override
    public boolean updateDepartment(Department department) {
        return changeDataInFile(Constants.departmentsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(department, data),
                fileModelHelper.modelToString(department));
    }

    @Override
    public Department removeDepartment(Department department) {
        return changeDataInFile(Constants.departmentsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(department, data), null) ? department : null;
    }

    @Override
    public List<Employee> getEmployees() {
        if (employees == null) {
            employees = new LinkedList<>();
            readDataFromFile(Constants.employeesFilePath, (br, line) -> {
                String[] empData = fileModelHelper.stringToEmployee(line);
                employees.add(new Employee(
                        Integer.parseInt(empData[0]),
                        empData[1],
                        empData[2],
                        empData[3],
                        EmployeeDesignation.valueOf(empData[4]),
                        Integer.parseInt(empData[5]),
                        Integer.parseInt(empData[6]),
                        Constants.dateParse(empData[7])));
            });
        }
        return employees;
    }

    @Override
    public Employee getEmployee(int employeeId) {
        for (Employee employee : getEmployees())
            if (employee.getId() == employeeId)
                return employee;
        return null;
    }

    @Override
    public Employee addEmployee(
            String username,
            String password,
            EmployeeDesignation designation,
            int departmentId,
            int mentorId) {
        int id = lastEmployeeId + 1;
        Employee employee = new Employee(id, username, username.trim().toLowerCase().replace(" ", ".")
                + id + "@" + Constants.corpName.toLowerCase()
                + "corp.com", password, designation, departmentId, mentorId);
        lastEmployeeId++;
        return appendDataInFile(Constants.employeesFilePath, fileModelHelper.modelToString(employee))
                && writeMiscellaneous() && getEmployees().add(employee) ? employee : null;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return changeDataInFile(Constants.employeesFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(employee, data),
                fileModelHelper.modelToString(employee));
    }

    @Override
    public Employee removeEmployee(Employee employee) {
        return changeDataInFile(Constants.employeesFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(employee, data), null) ? employee : null;
    }

    @Override
    public List<Report> getReports() {
        if (reports == null) {
            reports = new LinkedList<>();
            readDataFromFile(Constants.reportsFilePath, (br, line) -> {
                String[] reportData = fileModelHelper.stringToReport(line);
                reports.add(new Report(
                        Integer.parseInt(reportData[0]),
                        Integer.parseInt(reportData[1]),
                        reportData[2],
                        reportData[3],
                        Constants.dateParse(reportData[4])));
            });
        }
        return reports;
    }

    @Override
    public Report addReport(Report report) {
        return appendDataInFile(Constants.reportsFilePath, fileModelHelper.modelToString(report))
                && getReports().add(report) ? report : null;
    }

    @Override
    public boolean updateReport(Report report) {
        return changeDataInFile(Constants.reportsFilePath, (data) -> fileModelHelper.isPrimaryKeyEqual(report, data),
                fileModelHelper.modelToString(report));
    }

    @Override
    public Report removeReport(Report report) {
        return changeDataInFile(Constants.reportsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(report, data), null) ? report : null;
    }

    @Override
    public List<Product> getProducts() {
        if (products == null) {
            products = new LinkedList<>();
            readDataFromFile(Constants.productsFilePath, (bufferedReader, line) -> {
                String[] productData = fileModelHelper.stringToProduct(line);
                products.add(new Product(productData[0], productData[1]));
            });
        }
        return products;
    }

    @Override
    public Product getProduct(String productName) {
        for (Product product : getProducts())
            if (product.getName().equals(productName))
                return product;
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        return appendDataInFile(Constants.productsFilePath, fileModelHelper.modelToString(product))
                && getProducts().add(product) ? product : null;
    }

    @Override
    public boolean updateProduct(Product product) {
        return changeDataInFile(Constants.productsFilePath, (data) -> fileModelHelper.isPrimaryKeyEqual(product, data),
                fileModelHelper.modelToString(product));
    }

    @Override
    public Product removeProduct(Product product) {
        return changeDataInFile(Constants.productsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(product, data), null) ? product : null;
    }

    @Override
    public List<Client> getClients() {
        if (clients == null) {
            clients = new LinkedList<>();
            readDataFromFile(Constants.clientsFilePath, (br, line) -> {
                String[] clientData = fileModelHelper.stringToClient(line);
                clients.add(new Client(
                        clientData[0],
                        clientData[1],
                        clientData[2],
                        Constants.dateParse(clientData[3]),
                        Arrays.stream(clientData[4].split(",")).map(this::getProduct).collect(Collectors.toList())));
            });
        }
        return clients;
    }

    @Override
    public Client getClient(String email) {
        for (Client client : getClients())
            if (client.getEmail().equals(email))
                return client;
        return null;
    }

    @Override
    public Client addClient(Client client) {
        return appendDataInFile(Constants.clientsFilePath, fileModelHelper.modelToString(client))
                && getClients().add(client) ? client : null;
    }

    @Override
    public boolean updateClient(Client client) {
        return changeDataInFile(Constants.clientsFilePath, (data) -> fileModelHelper.isPrimaryKeyEqual(client, data),
                fileModelHelper.modelToString(client));
    }

    @Override
    public Client removeClient(Client client) {
        return changeDataInFile(Constants.clientsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(client, data), null) ? client : null;
    }

    @Override
    public List<Feedback> getFeedbacks() {
        if (feedbacks == null) {
            products = getProducts();
            clients = getClients();
            feedbacks = new LinkedList<>();
            readDataFromFile(Constants.feedbacksFilePath, (br, line) -> {
                System.out.println("line = " + line);
                String[] feedbackData = fileModelHelper.stringToFeedback(line);
                System.out.println(Arrays.toString(feedbackData));
                feedbacks.add(new Feedback(
                        feedbackData[0],
                        getProduct(feedbackData[1]),
                        getClient(feedbackData[2]),
                        Integer.parseInt(feedbackData[3]),
                        Constants.dateParse(feedbackData[4])));
            });
        }
        return feedbacks;
    }

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return appendDataInFile(Constants.feedbacksFilePath, fileModelHelper.modelToString(feedback))
                && getFeedbacks().add(feedback) ? feedback : null;
    }

    @Override
    public boolean updateFeedback(Feedback feedback) {
        return changeDataInFile(Constants.updatesFilePath, (data) -> fileModelHelper.isPrimaryKeyEqual(feedback, data),
                fileModelHelper.modelToString(feedback));
    }

    @Override
    public Feedback removeFeedback(Feedback feedback) {
        return changeDataInFile(Constants.updatesFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(feedback, data), null) ? feedback : null;
    }

    @Override
    public List<Ticket> getTickets() {
        if (tickets == null) {
            tickets = new LinkedList<>();
            readDataFromFile(Constants.ticketsFilePath, (br, line) -> {
                String[] ticketData = fileModelHelper.stringToTicket(line);
                tickets.add(new Ticket(
                        ticketData[0],
                        getProduct(ticketData[1]),
                        getClient(ticketData[2]),
                        TicketStatus.valueOf(ticketData[3]),
                        Integer.valueOf(ticketData[4]),
                        Constants.dateParse(ticketData[5])));
            });
        }
        return tickets;
    }

    @Override
    public Ticket addTicket(Ticket ticket) {
        return appendDataInFile(Constants.ticketsFilePath, fileModelHelper.modelToString(ticket))
                && getTickets().add(ticket) ? ticket : null;
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return changeDataInFile(Constants.ticketsFilePath, (data) -> fileModelHelper.isPrimaryKeyEqual(ticket, data),
                fileModelHelper.modelToString(ticket));
    }

    @Override
    public Ticket removeTicket(Ticket ticket) {
        return changeDataInFile(Constants.ticketsFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(ticket, data), null) ? ticket : null;
    }

    @Override
    public List<Question> getQuestions() {
        if (questions == null) {
            questions = new LinkedList<>();
            readDataFromFile(Constants.questionsFilePath, (br, line) -> {
                switch (line) {
                    case "MCQ":
                        int qLen = Integer.parseInt(readLine(br));
                        String q = readLine(br) + System.lineSeparator();
                        for (int i = 1; i < qLen; i++)
                            q = q.concat(System.lineSeparator()).concat(readLine(br));
                        questions.add(new Question.MCQ(q, readLine(br).split("~"), Integer.parseInt(readLine(br))));
                        break;
                    case "ESSAY":
                        questions.add(new Question.Essay(readLine(br), readLine(br).split(",")));
                        break;
                }
            });
        }
        return questions;
    }

    @Override
    public List<Update> getUpdates() {
        if (updates == null) {
            updates = new LinkedList<>();
            readDataFromFile(Constants.updatesFilePath, (br, line) -> {
                String[] updateData = fileModelHelper.stringToUpdate(line);
                updates.add(new Update(
                        updateData[0],
                        getProduct(updateData[1]),
                        Constants.dateParse(updateData[2])));
            });
        }
        return updates;
    }

    @Override
    public Update addUpdate(Update update) {
        return appendDataInFile(Constants.updatesFilePath, fileModelHelper.modelToString(update))
                && updates.add(update) ? update : null;
    }

    @Override
    public Update removeUpdate(Update update) {
        return changeDataInFile(Constants.updatesFilePath,
                (data) -> fileModelHelper.isPrimaryKeyEqual(update, data), null) ? update : null;
    }

    private boolean writeMiscellaneous() {
        try (PrintWriter printWriter = new PrintWriter(new File(Constants.miscelleneousFilePath))) {
            printWriter.println(lastDepartmentId);
            printWriter.println(lastEmployeeId);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean appendDataInFile(String toFilePath, String line) {
        try (FileWriter fileWriter = new FileWriter(toFilePath, true)) {
            fileWriter.append(line).append(System.lineSeparator());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void readDataFromFile(String fromFilePath, BiConsumer<BufferedReader, String> consumer) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFilePath))) {
            for (String line = ""; line != null; line = readLine(bufferedReader)) {
                if (line.isEmpty())
                    continue;
                consumer.accept(bufferedReader, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean changeDataInFile(String fromFilePath, Predicate<String> predicate, String updatedString) {
        try {
            File writeFile = File.createTempFile(fromFilePath, "temp");
            PrintWriter pw = new PrintWriter(writeFile);
            readDataFromFile(fromFilePath,
                    (br, line) -> writeln(pw, predicate.test(line) && updatedString != null ? updatedString : line));
            pw.close();
            if (new File(fromFilePath).delete()) {
                return writeFile.renameTo(new File(fromFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String readLine(BufferedReader bufferedReader) {
        try {
            return bufferedReader.readLine();
        } catch (NullPointerException | IOException e) {
            return null;
        }
    }

    private ModelDatabase writeln(PrintWriter printWriter, String line) {
        printWriter.println(line);
        return this;
    }

}
