package org.akash.messcup.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


public class UserService {
    Logger logger = Logger.getLogger(getClass().getName());
    private HashMap<String, String> employeeMap = new HashMap<>();


    public UserService() {
        String filePath = "C:/Mess/userData.txt";
        loadEmployeeData(filePath);
    }


    public String getEmpNameById(String empId) {
        return employeeMap.getOrDefault(empId,"Unknown Employee");
    }




    private void loadEmployeeData(String filePath) {
        try{
            List<String> lines= Files.readAllLines(Paths.get(filePath));
            for(String line:lines){
                String[] parts=line.split("\\|");
                if(parts.length==2){
                    employeeMap.put(parts[0].trim(),parts[1].trim());
                }

            }
            logger.info("Employee data loaded: " + employeeMap.size());

        }catch (Exception e){
            logger.info("Error loading employee data: "+e.getMessage());
        }
    }
}
