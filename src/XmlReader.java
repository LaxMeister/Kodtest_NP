import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlReader {
    private File xmlFile;
    public ValidationEmployee validationEmployee;
    public ValidationDepartment validationDepartment;
    public CreatePassCard createPassCard;
    private String base64Image;
    private String firstName;
    private String lastName;
    private String departmentName;
    private String location;

    public XmlReader(String filePath) {
        this.xmlFile = new File(filePath);
    }

    public void parseAndValidateXML() throws IOException {

        try {
            // Skapa en DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Läs in XML-filen som ett Document-objekt
            Document document = builder.parse(xmlFile);

            // Hämta alla Employee-element från XML-filen
            NodeList employeeList = document.getElementsByTagName("Employee");

            // Hämta alla Department-element från XML-filen
            NodeList departmentList = document.getElementsByTagName("Department");

            // Validera Employee-elementen
            validationEmployee = new ValidationEmployee(employeeList);

            // Validera Department-elementen
            validationDepartment = new ValidationDepartment(departmentList);

            //Gör en sista validering för att säkerställa att Department ID har en motsvarande Department som är giltigt och att ManagerID har en motsvarande Employee som är giltig.
            FinalValidation finalValidation = new FinalValidation().finalValidation(validationEmployee.employeeMap, validationDepartment.departmentHashMap);

            //Printar ut resultatet av all validering.
            System.out.println("\n------------------------------------------------------------------- ");
            System.out.println(" VALID EMPLOYEES\n");
            List<Employee> validEmployees = finalValidation.getValidEmployees(); // Assuming a getter method exists
            for (Employee employee : validEmployees) {
                base64Image = employee.getProfileImage();
                firstName = employee.getFirstName();
                lastName = employee.getLastName();
                System.out.println("Employee ID: " + employee.getEmployeeID());
                System.out.println("Firstname: " + employee.getFirstName());
                System.out.println("Lastname: " + employee.getLastName());
                System.out.println("Citizen Identifier: " +employee.getCitizenIdentifier());
                System.out.println("Employment Date: " + employee.getEmploymentDate());
                System.out.println("Department ID: " + employee.getDepartmentID());
                System.out.println("profile Image: " + "IS VALID");
                System.out.println("\n");
            }
            System.out.println("\n------------------------------------------------------------------- ");
            System.out.println(" VALID DEPARTMENTS\n");
            List<Department> validDepartments = finalValidation.getValidDepartments(); // Assuming a getter method exists
            for (Department department : validDepartments) {
                departmentName = department.getName();
                location = department.getLocation();
                System.out.println("Department ID: " + department.getDepartmentID());
                System.out.println("Name: " + department.getName());
                System.out.println("Manager ID: " + department.getManagerID());
                System.out.println("Location: " + department.getLocation());
                System.out.println("\n");
            }
            JFrame frame = new JFrame("Passerkort");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CreatePassCard(base64Image,firstName,lastName,departmentName,location)); // Skicka den dekodade profilbilden som argument
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
