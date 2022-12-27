package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.EmployeeDesignation;
import models.Employee;

public final class Constants {

    private Constants() {
    }

    public static final String corpName = "RV";

    private static final String currDir = "src/";
    private static final String dataDirName = currDir + "data/";

    public static final String clientsFilePath = dataDirName + "clients";
    public static final String departmentsFilePath = dataDirName + "departments";
    public static final String employeesFilePath = dataDirName + "employees";
    public static final String feedbacksFilePath = dataDirName + "feedbacks";
    public static final String miscelleneousFilePath = dataDirName + "miscellaneous";
    public static final String productsFilePath = dataDirName + "products";
    public static final String questionsFilePath = dataDirName + "questions";
    public static final String reportsFilePath = dataDirName + "reports";
    public static final String ticketsFilePath = dataDirName + "tickets";
    public static final String updatesFilePath = dataDirName + "updates";

    public static final Employee admin = new Employee(0, "Reshvanth V", "reshvanth.vm@rvcorp.com", "1a2b3c",
            EmployeeDesignation.ADMIN, 0, 0);
    
    public static final String clearScreen = "\033[H\033[2J";

    private static final String dateFormat = "E MMM d k:m:s z y";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    public static Date dateParse(String text) {
        try {
            return sdf.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
