package org.akash.messcup.service;

import javafx.scene.control.Alert;
import org.akash.messcup.controllers.MainController;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MealService {
    private int cupCount;
    private final String path="C:/Mess/mealData.txt";
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    Logger logger = Logger.getLogger(getClass().getName());
    LocalDate today = LocalDate.now();
    public String  setCupCount(String id, String mealTime, String empName) {
        if(empName.equals("Unknown Employee")){
            return "Unknown Employee!";
        }
        // Implementation to set cup count
        try {

            Path path1 = Paths.get(path);
            if (Files.notExists(path1)) {
                Files.createFile(path1);
            }
            List<String> lines = Files.readAllLines(path1);
            boolean exists=lines.stream()
                            .anyMatch(line->line.startsWith(id + "|") && line.contains("|" + mealTime + "|" + today + "|"));
            if(exists){
                return "Duplicate entry! User already exists.";
            }
            cupCount=(int)lines.stream()
                            .filter(line->line.startsWith(id + "|") && line.contains("|" + mealTime + "|" + today + "|"))
                                    .count()+1;
            String entry = id + "|" + empName + "|" + mealTime +"|"+ today +"|"+ cupCount + "\n";


            Files.write(path1,entry.getBytes(), StandardOpenOption.APPEND);
            return "";

        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error occurred", e);
            return "Error saving data: " + e.getMessage();
        }
        

    }

    public int getCupCount() {
        return cupCount;
    }

    public void generatePdfReport() {
        String pdfPath = "C:/Mess/" + today + "/mealReport.pdf";
        Path pdfpath = Paths.get("C:/Mess/" + today);
        // Implementation to generate PDF report
        try ( PDDocument document = new PDDocument()){
            // Read all lines from txt
            List<String> lines = Files.readAllLines(Paths.get(path));

            if (Files.notExists(pdfpath)) {
                Files.createDirectories(pdfpath);   // creates all missing folders
            }

            // Create new PDF

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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Your PDF has been created at:\n" + pdfPath);
            alert.showAndWait();

            logger.info("PDF created successfully");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred", e);
        }
    }
}
