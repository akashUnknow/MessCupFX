package org.akash.messcup.service;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
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
import java.util.logging.*;

public class MealService {

    private int cupCount;
    private static final Logger LOGGER = Logger.getLogger(MealService.class.getName());
    private final LocalDate today = LocalDate.now();
    private final String path = "C:/Mess/" + today + "/mealData.txt";

    static {
        try {
            Files.createDirectories(Paths.get("logs"));
            FileHandler fh = new FileHandler("logs/messcup.log", 0, 1, true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the cup count for an employee for a given meal time.
     *
     * @param id        Employee ID
     * @param mealTime  Meal time (Breakfast/Lunch/Dinner)
     * @param empName   Employee name
     * @return Error message if any, empty string if success
     */
    public String setCupCount(String id, String mealTime, String empName) {
        if ("Unknown Employee".equals(empName)) {
            LOGGER.warning("Unknown employee tried to set cup count: " + id);
            return "Unknown Employee!";
        }

        try {
            Path path1 = Paths.get(path);
            Path parentDir = path1.getParent();
            if (Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
                LOGGER.info("Created directory: " + parentDir);
            }
            if (Files.notExists(path1)) {
                Files.createFile(path1);
                LOGGER.info("Created file: " + path1);
            }

            List<String> lines = Files.readAllLines(path1);

            boolean exists = lines.stream()
                    .anyMatch(line -> line.startsWith(id + "|") && line.contains("|" + mealTime + "|" + today + "|"));
            if (exists) {
                LOGGER.warning("Duplicate entry attempted for ID: " + id + " Meal: " + mealTime);
                return "Duplicate entry! User already exists.";
            }

            cupCount = (int) lines.stream()
                    .filter(line -> line.contains(today + "|"))
                    .count() + 1;

            String entry = id + "|" + empName + "|" + mealTime + "|" + today + "|" + cupCount + "\n";
            Files.write(path1, entry.getBytes(), StandardOpenOption.APPEND);

            LOGGER.info("Cup count set successfully for ID: " + id + ", Meal: " + mealTime + ", Count: " + cupCount);

            return "";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving cup count for ID: " + id, e);
            return "Error saving data: " + e.getMessage();
        }
    }

    public int getCupCount() {
        return cupCount;
    }

    /**
     * Generates a PDF report of the current day's meal data.
     */
    public void generatePdfReport() {
        String pdfPath = "C:/Mess/" + today + "/mealReport.pdf";
        Path pdfDir = Paths.get("C:/Mess/" + today);

        try {
            if (Files.notExists(pdfDir)) {
                Files.createDirectories(pdfDir);
                LOGGER.info("Created directory for PDF report: " + pdfDir);
            }

            List<String> lines = Files.readAllLines(Paths.get(path));
            try (PDDocument document = new PDDocument()) {

                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream content = new PDPageContentStream(document, page);
                content.setFont(PDType1Font.HELVETICA, 14);
                float y = 750;

                for (String line : lines) {
                    content.beginText();
                    content.newLineAtOffset(50, y);
                    content.showText(line);
                    content.endText();

                    y -= 20;
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
                LOGGER.info("PDF report generated successfully at: " + pdfPath);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Your PDF has been created at:\n" + pdfPath);
                alert.showAndWait();
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF report", e);
        }
    }
}
