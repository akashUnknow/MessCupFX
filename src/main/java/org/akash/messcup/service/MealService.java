package org.akash.messcup.service;

import javafx.scene.control.Alert;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

public class MealService {
    private int cupCount;
    private String path="C:/Mess/mealData.txt";
    private String pdfPath;
    LocalDate today = LocalDate.now();
    public String  setCupCount(String id, String mealTime, String empName) {
        if(empName.equals("Unknown Employee")){
            return "Unknown Employee!";
        }
        // Implementation to set cup count
        try {

            if (Files.notExists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
            }
            List<String> lines = Files.readAllLines(Paths.get(path));
            boolean exists=lines.stream()
                            .anyMatch(line->line.startsWith(id + "|") && line.contains("|" + mealTime + "|" + today + "|"));
            if(exists){
                return "Duplicate entry! User already exists.";
            }
            cupCount=(int)lines.stream()
                            .filter(line->line.startsWith(id + "|") && line.contains("|" + mealTime + "|" + today + "|"))
                                    .count()+1;
            String entry = id + "|" + empName + "|" + mealTime +"|"+ today +"|"+ cupCount + "\n";


            Files.write(Paths.get(path),entry.getBytes(), StandardOpenOption.APPEND);
            return "";

        }catch (Exception e){
            e.printStackTrace();
            return "Error saving data: " + e.getMessage();
        }
        

    }

    public int getCupCount() {
        return cupCount;
    }

    public void generatePdfReport() {
        String pdfPath = "C:/Mess/" + today + "/mealReport.pdf";
        Path PDFpath = Paths.get("C:/Mess/" + today);
        // Implementation to generate PDF report
        try {
            // Read all lines from txt
            List<String> lines = Files.readAllLines(Paths.get(path));

            if (Files.notExists(PDFpath)) {
                Files.createDirectories(PDFpath);   // creates all missing folders
            }

            // Create new PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);
            content.setFont(PDType1Font.HELVETICA, 14);

            float y = 750;  // Starting y position

            for (String line : lines) {
                content.beginText();
                content.newLineAtOffset(50, y);
                content.showText(line);
                content.endText();

                y -= 20; // Move down per line

                // If page full → create new page
                if (y < 50) {
                    content.close();
                    page = new PDPage();
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    content.setFont(PDType1Font.HELVETICA, 14);
                    y = 750;
                }
            }

            content.close();
            document.save(pdfPath);
            document.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Your PDF has been created at:\n" + pdfPath);
            alert.showAndWait();

            System.out.println("PDF created successfully at: " + pdfPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
