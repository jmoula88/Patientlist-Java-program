//Jason Wada
//COP2552.0M1
// Patient records project


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient {

    public static void main(String[] args) throws IOException {
        // Create an instance of the Patient class and invoke the processRecords method
        Patient patientManager = new Patient();
        patientManager.processRecords();
    }

    public void processRecords() throws IOException {
        // Get the current date
        String currentDate = getCurrentDate();

        // Define file paths for input and output
        String patientW3Path = "./PatientListW3.txt";
        String newPatientPath = "./NewPatientList.txt";
        String removePatientPath = "./RemovePatientList.txt";

        // Create FileWriter objects for output files
        FileWriter patientW4Writer = new FileWriter("C:\\SFC\\COP2552\\Project2\\PatientListW4.txt");
        FileWriter patientErrorsWriter = new FileWriter("C:\\SFC\\COP2552\\Project2\\PatientErrorsW4.txt");

        // Write the current date to the output files
        patientW4Writer.write(currentDate + "\n");
        patientErrorsWriter.write(currentDate + "\n");

        // Process patient records
        processPatientRecords(patientW3Path, newPatientPath, removePatientPath,
                patientW4Writer, patientErrorsWriter);

        // Close FileWriter objects
        patientW4Writer.close();
        patientErrorsWriter.close();

        // Display a message indicating that patient records have been updated
        System.out.println("Patient records have been updated");
    }

    // Method to compare patient IDs and determine the operation to be performed
    // 0: Add currentPatientrecord to w4 file
    // 1: Add newPatientRecord to w4 file
    // 2: Remove from patientListW3 and add to error file
    // 3: No more patients
    public int compareId(String currentPatientRecord, String newPatientRecord, String removePatientRecord) {
        if (currentPatientRecord == null) {
            if (newPatientRecord == null) {
                return 3;
            } else {
                return 1;
            }
        }
        if (Integer.parseInt(currentPatientRecord) == Integer.parseInt(removePatientRecord)) {
            return 2;
        }
        if (Integer.parseInt(currentPatientRecord) > Integer.parseInt(newPatientRecord)) {
            return 1;
        } else {
            return 0;
        }
    }

    // Method to write patient information to a file based on the operation type
    // If newOrNot is 1, it's a new patient, and the current date is added
    // If newOrNot is 0, it's an existing patient, and additional information is written
    public void writeToFile(String id, Scanner scanner, FileWriter file, int newOrNot) throws IOException {
        file.write(id + "\n");
        file.write(scanner.nextLine() + "\n");
        file.write(scanner.nextLine() + "\n");
        if (newOrNot == 1) {
            file.write(getCurrentDate());
        } else {
            file.write(scanner.nextLine() + "\n");
        }
    }

    // Method to process patient records from three input files
    private void processPatientRecords(String patientW3Path, String newPatientPath,
                                       String removePatientPath, FileWriter patientW4Writer,
                                       FileWriter patientErrorsWriter) throws IOException {

        // Create Scanner objects for input files
        Scanner patientW3Scanner = new Scanner(new File(patientW3Path));
        Scanner newPatientScanner = new Scanner(new File(newPatientPath));
        Scanner removePatientScanner = new Scanner(new File(removePatientPath));

        // Skip the previous date in PatientListW3.txt
        patientW3Scanner.nextLine();

        // Initialize variables for current patient records from different files
        String currentPatientRecord = readNextRecord(patientW3Scanner);
        String newPatientRecord = readNextRecord(newPatientScanner);
        String removePatientRecord = readNextRecord(removePatientScanner);

        // Compare patient IDs and determine the initial operation
        int compareResult = compareId(currentPatientRecord, newPatientRecord, removePatientRecord);

        // Continue processing until there are no more patients
        while (compareResult != 3) {

            if (compareResult == 0) {
                // Add currentPatientrecord to w4 file
                writeToFile(currentPatientRecord, patientW3Scanner, patientW4Writer, 0);
                currentPatientRecord = readNextRecord(patientW3Scanner);

            }
            if (compareResult == 1) {
                // Add newPatientRecord to w4 file
                writeToFile(newPatientRecord, newPatientScanner, patientW4Writer, 1);
                newPatientRecord = readNextRecord(newPatientScanner);

            }
            if (compareResult == 2) {
                // Remove from patientListW3 and add to error file
                patientW3Scanner.nextLine();
                patientW3Scanner.nextLine();
                patientW3Scanner.nextLine();
                currentPatientRecord = readNextRecord(patientW3Scanner);

                removePatientScanner.nextLine();
                removePatientScanner.nextLine();
                removePatientRecord = readNextRecord(removePatientScanner);
            }

            // Update compareResult for the next iteration
            compareResult = compareId(currentPatientRecord, newPatientRecord, removePatientRecord);
        }

        // Write remaining patient from removePatientList to error file
        writeToError(removePatientRecord, removePatientScanner, patientErrorsWriter);
    }

    // Method to write remaining patient from removePatientList to error file
    public void writeToError(String id, Scanner scanner, FileWriter file) throws IOException {
        file.write(id + "\n");
        file.write(scanner.nextLine() + "\n");
        file.write(scanner.nextLine() + "\n");

        // Write remaining lines to the error file
        while (scanner.hasNextLine()) {
            file.write(scanner.nextLine() + "\n");
        }
    }

    // Method to get the current date in MMDDYYYY format
    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        String formattedDate = currentDate.format(formatter);
        return formattedDate + "\n";
    }

    // Method to read the next record from a Scanner
    private String readNextRecord(Scanner scanner) {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }
}
