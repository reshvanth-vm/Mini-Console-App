package models;

import java.util.Date;

import enums.EmployeeDesignation;

public final class Employee extends User {
    private final int id;
    private int mentorId, departmentId;
    private EmployeeDesignation designation;

    public Employee(
            int id,
            String username,
            String email,
            String password,
            EmployeeDesignation designation,
            int departmentId,
            int mentorId) {
        super(username, email, password);
        this.id = id;
        this.designation = designation;
        this.departmentId = departmentId;
        this.mentorId = mentorId;
    }

    public Employee(
            int id,
            String name,
            String email,
            String password,
            EmployeeDesignation designation,
            int departmentId,
            int mentorId,
            Date lastLogin) {
        this(id, name, email, password, designation, departmentId, mentorId);
        this.lastLogin = lastLogin;
    }

    public int getId() {
        return id;
    }

    public EmployeeDesignation getDesignation() {
        return designation;
    }

    public void setDesignation(EmployeeDesignation employeeDesignation) {
        this.designation = employeeDesignation;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

}
