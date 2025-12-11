package org.akash.messcup.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


public class UserService {
    private HashMap<String, String> employeeMap = new HashMap<>();
    private final String filePath="C:/Mess/userData.txt";


    public UserService() {
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
            System.out.println("Employee data loaded: " + employeeMap.size());

        }catch (Exception e){
            System.out.println("Error loading employee data: "+e.getMessage());
        }
    }
}
