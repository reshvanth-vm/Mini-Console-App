package ui;

import enums.EmployeeDesignation;
import helpers.Constants;
import helpers.Input;
import models.Department;
import models.Employee;
import models.Question;
import repository.EmployeeRepository;

public class EmployeeSignUp implements SignUp {
    private EmployeeRepository employeeRepository;
    private String employeeName, employeePassword;

    public EmployeeSignUp(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void greet() {
        System.out.println("That's a very great idea to choose " + Constants.corpName
                + " corp to work in as an Employee");
    }

    @Override
    public boolean isReadyToQualify() {
        System.out.println("But before we can take you in as an Employee you need to qualify as one");
        System.out.println(
                "For that you are now going to take a TEST which will be comprised of 5 MCQ's and 2 ESSAY type questions!");
        System.out.println("So let's start the test! ;)");
        System.out.println();
        return Input.getYesOrNoAsBoolean("Are you ready [Y/n] >> ");
    }

    @Override
    public boolean isQualifiedToJoin() {
        do {
            Question[] questions = employeeRepository.getQuestions();
            int questionNo = 1, correctAnswers = 0;
            for (Question q : questions) {
                System.out.println("Question No : " + questionNo++);
                System.out.println();
                System.out.println(q.toString());
                correctAnswers += q.isCorrect(System.console().readLine("Type ans >> ").trim()) ? 1 : 0;
                System.out.println(Constants.clearScreen);
            }
            if (correctAnswers >= 4) {
                System.out.println("Congratulations!");
                System.out.println("You have succesfully cracked the exam!");
                return true;
            }
            System.out.println("Sorry, you didn't perform well to join as an Employee!");
        } while (Input.getYesOrNoAsBoolean("Do you want to take retest? [y/N] >> "));
        System.out.println("Bye, have a better luck next time!");
        return false;
    }

    @Override
    public boolean gatherDetails() {
        System.out.println("Your informations are necessary to onboard you to this company");
        System.out.println("So feel free to give that so you can enter in this company happily");
        System.out.println();
        employeeName = Input.getUsername("Enter your name (with initial at last) >> ");
        employeePassword = Input.getPasswordAfterConformPassword();
        System.out.println();
        System.out.println("A new email related to this corp will be generated for you!");
        return Input.getYesOrNoAsBoolean("Do you really wish to join this company [y/n] >> ");
    }

    @Override
    public void join() {
        Department department = employeeRepository.getDepartmentOfLowestEmployeeCount();
        if (department == null) {
            System.out.println("Sorry there are no departments (no vacancies) available right now!");
            return;
        }
        Employee employee = employeeRepository.addEmployee(employeeName, employeePassword,
                EmployeeDesignation.MEMBER_TECHNICAL_STAFF, department.getId(),
                employeeRepository.getMentorIdForNewEmployee(department));
        if (employee == null) {
            System.out.println("There is a technical issue on recruting you!");
            Input.waitUntilReturn();
            return;
        }
        System.out.println(Constants.clearScreen);
        System.out.println("Congratulation once more!");
        System.out.println("Here are your credentials to note down!");
        System.out.println();
        System.out.println("Employee Id : " + employee.getId());
        System.out.println("Name        : " + employee.getUsername());
        System.out.println("Email       : " + employee.getEmail());
        System.out.println("Department  : " + department.getName());
        System.out.println("Designation : " + employee.getDesignation());
        System.out.println("Mentor      : " + employeeRepository.getEmployee(employee.getMentorId()).getUsername()
                + "(id: " + employee.getMentorId() + ")");
        System.out.println("Please note down these information to help yourself on login!");
        Input.waitUntilReturn();
        System.out.println("Please note down these information to help yourself on login!");
        Input.waitUntilReturn();
    }

}
