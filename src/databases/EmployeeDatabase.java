package databases;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import enums.EmployeeDesignation;
import models.Department;
import models.Employee;
import models.Product;
import models.Question;
import models.Report;
import models.Update;
import repository.EmployeeRepository;
import repository.ModelRepository;

public class EmployeeDatabase implements EmployeeRepository {
    private static EmployeeDatabase employeeRepos;
    private ModelRepository modelRepository;

    private Map<String, Employee> emailEmployeeMap;

    private EmployeeDatabase(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
        emailEmployeeMap = new HashMap<>();
        for (Employee employee : modelRepository.getEmployees())
            emailEmployeeMap.put(employee.getEmail(), employee);
    }

    public static EmployeeDatabase getInstance(ModelRepository modelRepository) {
        if (employeeRepos == null)
            employeeRepos = new EmployeeDatabase(modelRepository);
        return employeeRepos;
    }

    @Override
    public Department addDepartment(String departmentName, Product product, int managerId) {
        return modelRepository.addDepartment(departmentName, product, managerId);
    }

    @Override
    public Department getDepartment(int departmentId) {
        return modelRepository.getDepartment(departmentId);
    }

    @Override
    public List<Department> getDepartments() {
        return modelRepository.getDepartments();
    }

    @Override
    public Employee getEmployee(int employeeId) {
        return modelRepository.getEmployee(employeeId);
    }

    @Override
    public Employee getEmployee(String email, String password) {
        Employee employee = emailEmployeeMap.get(email);
        if (employee != null && employee.getPassword().equals(password))
            return employee;
        return null;
    }

    @Override
    public List<Employee> getEmployees() {
        return modelRepository.getEmployees();
    }

    @Override
    public Employee addEmployee(String username, String password, EmployeeDesignation designation, int departmentId,
            int mentorId) {
        return modelRepository.addEmployee(username, password, designation, departmentId, mentorId);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return modelRepository.updateEmployee(employee);
    }

    @Override
    public Department getDepartmentOfLowestEmployeeCount() {
        Map<Integer, Integer> departmentEmployeeCountMap = new HashMap<>();
        for (Employee employee : modelRepository.getEmployees()) {
            Integer employeeCount = departmentEmployeeCountMap.get(employee.getDepartmentId());
            departmentEmployeeCountMap.put(employee.getDepartmentId(), employeeCount == null ? 1 : employeeCount + 1);
        }
        if (departmentEmployeeCountMap.isEmpty())
            return null;
        int minimumEmployeeCount = modelRepository.getEmployees().size();
        int departmentId = 0;
        for (Map.Entry<Integer, Integer> map : departmentEmployeeCountMap.entrySet()) {
            if (map.getValue() <= minimumEmployeeCount) {
                departmentId = map.getKey();
                minimumEmployeeCount = map.getValue();
            }
        }
        return modelRepository.getDepartment(departmentId);
    }

    @Override
    public int getMentorIdForNewEmployee(Department department) {
        int newMentorId = department.getManagerId();
        Map<Integer, Integer> noOfTraineesUnderMentor = new HashMap<>();
        for (Employee employee : modelRepository.getEmployees()) {
            if (employee.getDepartmentId() == department.getId()) {
                if (!employee.getDesignation().equals(EmployeeDesignation.MANAGER)) {
                    Integer noOfTrainees = noOfTraineesUnderMentor.get(employee.getMentorId());
                    noOfTraineesUnderMentor.put(employee.getMentorId(), noOfTrainees == null ? 1 : noOfTrainees + 1);
                }
            }
        }
        int minimumTraineeCount = modelRepository.getEmployees().size();
        for (Map.Entry<Integer, Integer> map : noOfTraineesUnderMentor.entrySet()) {
            if (map.getValue() < minimumTraineeCount) {
                newMentorId = map.getKey();
                minimumTraineeCount = map.getValue();
            }
        }
        return newMentorId;
    }

    @Override
    public Question[] getQuestions() {
        int mcqCount = 5, essayCount = 2;
        Question[] questions = new Question[mcqCount + essayCount];
        int bound = modelRepository.getQuestions().size();
        Random random = new Random();
        Set<Integer> questionIndices = new HashSet<>(questions.length);
        for (int i = 0; i < questions.length;) {
            int questionIndex = random.nextInt(bound);
            if (questionIndices.contains(questionIndex))
                continue;
            Question question = modelRepository.getQuestions().get(questionIndex);
            if ((i < mcqCount && question instanceof Question.MCQ)
                    || (i >= mcqCount && question instanceof Question.Essay)) {
                if (questionIndices.add(questionIndex))
                    questions[i++] = question;
            }
        }
        return questions;
    }

    @Override
    public Report addReport(Report report) {
        return modelRepository.addReport(report);
    }

    @Override
    public List<Report> getReports(int from, int to) {
        return modelRepository.getReports().stream().filter(
                (report) -> report.getFromEmployeeId() == from && report.getToEmployeeId() == to)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getReportsSent(int from) {
        return modelRepository.getReports().stream().filter(
                (report) -> report.getFromEmployeeId() == from).collect(Collectors.toList());
    }

    @Override
    public Update releaseAnUpdate(Update update) {
        return modelRepository.addUpdate(update);
    }

    @Override
    public List<Report> getReportsReceived(int to) {
        return modelRepository.getReports().stream().filter(
                (report) -> report.getToEmployeeId() == to).collect(Collectors.toList());
    }

}
