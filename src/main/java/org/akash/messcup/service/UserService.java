package org.akash.messcup.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private HashMap<String, String> employeeMap = new HashMap<>();

    static {
        try {
            java.nio.file.Files.createDirectories(Paths.get("logs"));
            FileHandler fh = new FileHandler("logs/messcup.log", 0, 1, true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserService() {
        String filePath = "C:/Mess/userData.txt";
        loadEmployeeData(filePath);
    }

    public String getEmpNameById(String empId) {
        String empName = employeeMap.getOrDefault(empId, "Unknown Employee");
        LOGGER.info("Lookup employee ID: " + empId + " → " + empName);
        return empName;
    }

    private void loadEmployeeData(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    employeeMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            LOGGER.info("Employee data loaded successfully: " + employeeMap.size() + " employees");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading employee data from: " + filePath, e);
        }
    }
}
