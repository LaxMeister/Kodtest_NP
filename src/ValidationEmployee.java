import Utils.ErrorLogger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationEmployee {
    public HashSet<Integer> employeeIDs = new HashSet<>();
    public HashSet<Integer> employee_DepartmentIDs = new HashSet<>();
    private HashSet<String> citizenIdentifiers = new HashSet<>();
    public HashMap<Integer, Employee> employeeMap = new HashMap<Integer, Employee>();
    public int employeeID;
    public String firstName = "";
    public String lastName = "";
    public String citizenIdentifier = "";
    public String employmentDate = "";
    public Integer employee_DepartmentID;
    public String profileImage = "";

    public ValidationEmployee(NodeList list) throws IOException {
        NodeList employeeList = list;

        try {
            // Iterera genom varje Employee-element och validera det
            for (int i = 0; i < employeeList.getLength(); i++) {
                Element employeeElement = (Element) employeeList.item(i);

                // Booleans för att hålla reda på om varje del av anställd-informationen är giltig
                boolean isValidEmployeeID = false;
                boolean isValidFirstName = false;
                boolean isValidLastName = false;
                boolean isValidCitizenIdentifier = false;
                boolean isValidEmploymentDate = false;
                boolean isValidDepartmentID = false;
                boolean isValidProfileImage = false;

                // Validera anställnings-ID
                isValidEmployeeID = validateID(employeeElement);

                // Om anställnings-ID är giltigt, fortsätt att validera andra delar av anställd-informationen
                if (isValidEmployeeID) {
                    isValidFirstName = validateFirstname(employeeElement);
                    isValidLastName = validateLastname(employeeElement);
                    isValidCitizenIdentifier = validateCitizenIdentifier(employeeElement);
                    isValidEmploymentDate = validateEmploymentDate(employeeElement);
                    isValidDepartmentID = validateDepartmentId(employeeElement, employee_DepartmentIDs);
                    isValidProfileImage = isBase64EncodedImage(employeeElement);
                }

                // Om all information är giltig, skapa ett Employee-objekt och lägg till i employeeMap
                if (isValidEmployeeID && isValidFirstName && isValidLastName && isValidCitizenIdentifier && isValidEmploymentDate && isValidDepartmentID && isValidProfileImage) {
                    Employee validEmployee = new Employee(employeeID, firstName, lastName, citizenIdentifier, employmentDate, employee_DepartmentID, profileImage);
                    employeeMap.put(employeeID, validEmployee);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.logError(e.getMessage()); // loggar eventuella undantag
        }

    }

    // Kontrollera om employeeID finns, är unikt och är inte tom.
    private boolean validateID(Element employeeElement) {
        try {
            // Hämta och konverterar anställnings-ID till int från XML-data
            employeeID = Integer.parseInt(employeeElement.getElementsByTagName("employeeID").item(0).getTextContent());
            int ID = employeeID;
            // Kontrollera om anställnings-ID är inom det tillåtna intervallet och unikt
            if (ID > 0 && ID < 9999) {
                if (employeeIDs.contains(ID)) {
                    ErrorLogger.logError("Error: Duplicate employee ID found: " + ID);
                    return false; // Hoppa över posten om ID inte är unikt
                } else {
                    employeeIDs.add(ID); // Lägg till ID i HashSet om det är unikt
                    return true;
                }
            } else {
                ErrorLogger.logError("Error: ID is missing or empty for an employee.");
                return false; // Hoppa över posten om ID saknas eller är tomt
            }
        } catch (NumberFormatException e) {
            ErrorLogger.logError("Error: ID is not a valid number.");
            return false; // Hoppa över posten om ID inte är en giltig siffra
        }

    }

    private boolean validateFirstname(Element employeeElement) {
        // Kontrollera om förnamnet finns och inte överstiger 20 tecken
        NodeList firstnameNodes = employeeElement.getElementsByTagName("firstname");
        if (firstnameNodes.getLength() > 0) {
            firstName = firstnameNodes.item(0).getTextContent().trim();
            if (firstName.isEmpty() || firstName.length() > 20) {
                ErrorLogger.logError("Error: firstname is missing, too long, or empty for an employee.");
                return false; // Hoppa över post om förnamnet inte finns eller är för långt.
            }
        } else {
            ErrorLogger.logError("Error: firstname is missing or empty for an employee.");
            return false; /// Hoppa över post om förnamnet inte finns.
        }

        return true;
    }

    private boolean validateLastname(Element employeeElement) {

        // Kontrollera om efternamn finns och inte överstiger 20 tecken
        NodeList lastnameNodes = employeeElement.getElementsByTagName("lastname");
        if (lastnameNodes.getLength() > 0) {
            lastName = lastnameNodes.item(0).getTextContent().trim();
            if (lastName.isEmpty() || lastName.length() > 20) {
                ErrorLogger.logError("Error: lastname is missing, too long, or empty for an employee.");
                return false; // Hoppa över post om efternamn inte finns eller är för långt.
            }
        } else {
            ErrorLogger.logError("Error: lastname is missing or empty for an employee.");
            return false; // Hoppa över post om efternamn inte finns.
        }

        return true;
    }


    private boolean validateCitizenIdentifier(Element employeeElement) {
        citizenIdentifier = employeeElement.getElementsByTagName("citizenIdentifier").item(0).getTextContent();
        try {
            // Kontrollera om personnummer finns och inte är tomt
            if (citizenIdentifier.isEmpty()) {
                ErrorLogger.logError("Error: Citizenidentifier is missing");
                return false;
            }

            // Kontrollera att personnummer har korrekt format YYYYMMDD-XXX
            Pattern pattern = Pattern.compile("^\\d{4}\\d{2}\\d{2}-\\d{4}$");
            Matcher matcher = pattern.matcher(citizenIdentifier);
            if (!matcher.matches()) {
                ErrorLogger.logError("CitizenIdentifier has wrong format. Most be in this format YYYYMMDD-XXXX");
                return false; // Hoppa över post det är fel format på personnummer
            }

            // kontrollerar att personnummer är unikt
            if (citizenIdentifiers.contains(citizenIdentifier)) {
                ErrorLogger.logError("Error: Duplicate employee citizen identifier found: " + citizenIdentifier);
                return false; // Hoppar över post om personnummer inte är unikt
            } else {
                citizenIdentifiers.add(citizenIdentifier); // Lägg till personnummer i HashSet om det är unikt
                return true;
            }

        } catch (Exception e) {
            ErrorLogger.logError("Error! something went wrong here --> " + e); // loggar eventuella undantag
        }
        return true;
    }

    private boolean validateEmploymentDate(Element employeeElement) {
        try {
            // Hämta anställningsdatum från XML-data
            NodeList employmentDateNodeList = employeeElement.getElementsByTagName("employmentDate");

            // Kontrollera om anställningsdatum finns
            if (employmentDateNodeList.getLength() == 0) {
                ErrorLogger.logError("Error: employmentDate element not found");
                return false; // Hoppa över post om anställningsdatum inte hittades
            }

            // Hämta det första anställningsdatum-elementet
            Node employmentDateNode = employmentDateNodeList.item(0);


            // Kontrollera om anställningsdatum-elementet inte är null
            if (employmentDateNode == null) {
                ErrorLogger.logError("Error: employmentDate element is null");
                return false; // Hoppa över post om anställningsdatum är nullt
            }

            employmentDate = employmentDateNode.getTextContent();

            // kontrollerar att det är rätt format på anställningsdatum YYYYMMDD
            Pattern pattern = Pattern.compile("^\\d{4}\\d{2}\\d{2}");
            Matcher matcher = pattern.matcher(employmentDate);

            if (!matcher.matches()) {
                ErrorLogger.logError("employmentDate has wrong format. Most be in this format YYYYMMDD");
                return false; // Hoppa över post om anställningsdatum har fel format
            }

            // Kontrollera att anställningsdatum inte är i framtiden eller före en viss datumgräns
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate date = LocalDate.parse(employmentDate.substring(0, 8), formatter);
            if (date.isAfter(LocalDate.now())) {
                ErrorLogger.logError("Error: employmentDate date is in the future: " + employmentDate);
                return false; // Hoppa över post om anställningsdatum är i framtiden
            } else if (date.isBefore(LocalDate.of(2010, 7, 21))) {
                ErrorLogger.logError("Error: employmentDate date is before 2010-07-21: " + employmentDate);
                return false; // Hoppa över post om anställningsdatum är före datumgräns
            }
        } catch (DateTimeParseException e) {
            ErrorLogger.logError("Error parsing date from employmentDate: " + e); // loggar eventuella undantag
            return false;
        }
        return true;

    }

    private boolean validateDepartmentId(Element employeeElement, HashSet departmentIDs) {
        try {
            // Hämta och konverterar avdelnings-ID till int från XML-data
            employee_DepartmentID = Integer.parseInt(employeeElement.getElementsByTagName("departmentID").item(0).getTextContent());
            int employeeDepartmentID = employee_DepartmentID;

            // Kontrollera om avdelnings-ID är inom det tillåtna intervallet
            if (employeeDepartmentID > 0 && employeeDepartmentID < 9999) {
                departmentIDs.add(employeeDepartmentID); // Lägg till ID i HashSet om det är unikt
            } else {
                ErrorLogger.logError("Error: ID is missing or empty for an manager.");
                return false;
            }
        } catch (NumberFormatException e) {
            ErrorLogger.logError("Error: ID is not a valid number.");
            return false; // Hoppa över posten om ID inte är en giltig siffra

        } catch (NullPointerException n) {
            ErrorLogger.logError("Error: managerID is null"); // loggar eventuella undantag
            return false;
        }

        return true;
    }

    public boolean isBase64EncodedImage(Element employeeElement) {
        try {
            try {
                profileImage = employeeElement.getElementsByTagName("profileImage").item(0).getTextContent();
            } catch (NullPointerException e) {
                ErrorLogger.logError("Image is null");
            }
            // Kontrollera att strängen inte är null eller tom.
            if (profileImage == null || profileImage.isEmpty()) {
                return false;
            }

            // Decode:a strängen till en byte-array.
            byte[] decodedBytes = Base64.getDecoder().decode(profileImage);

            // Kontrollera om byte-array:en är tom.
            if (decodedBytes.length == 0) {
                return false;
            }

            // Extrahera tillräckligt med bytes för att matcha den längsta av PNG- och JPEG-filsignaturerna
            byte[] signatureBytes = Arrays.copyOfRange(decodedBytes, 0, 8);

            // Skapa byte-arrays med de förväntade värdena för PNG- och JPEG-filsignaturer
            byte[] pngSignature = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
            byte[] jpegSignature = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, (byte) 0x00, (byte) 0x10, (byte) 0x4A, (byte) 0x46};

            // Jämför de extraherade byten med de förväntade värdena för både PNG- och JPEG-filsignaturer
            if (Arrays.equals(signatureBytes, pngSignature) || Arrays.equals(signatureBytes, jpegSignature)) {
                return true; // Detta indikerar att den dekodade datan representerar antingen en PNG- eller en JPEG-bild
            } else {
                return false; // Detta indikerar att den dekodade datan inte representerar någon av de två bildformaten
            }
        } catch (IllegalArgumentException e) {
            ErrorLogger.logError("Error: not a valid image");
        }

        return true;
    }


}
