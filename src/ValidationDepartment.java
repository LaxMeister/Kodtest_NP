import Utils.ErrorLogger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ValidationDepartment {
    private HashSet<Integer> departmentIDs = new HashSet<>();
    public HashSet<Integer> managerIDs = new HashSet<>();
    private String departmentLocation = "";
    private int departmentID;
    private String departmentName = "";
    private int managerID;

    public ArrayList<Department> departmentArray = new ArrayList<>();
    public HashMap<Integer, Department> departmentHashMap = new HashMap<Integer, Department>();


    public ValidationDepartment(NodeList list) {
        NodeList departmentList = list;
        try {
            // Iterera genom varje Department-element och validera det
            for (int i = 0; i < departmentList.getLength(); i++) {
                Element departmentElement = (Element) departmentList.item(i);

                // Booleska flaggor för att spåra giltigheten av varje attribut
                boolean isValidDepartmentID = false;
                boolean isValidDepartmentName = false;
                boolean isValidManagerID = false;
                boolean isValidDepartmentLocation = false;

                // Validera avdelnings-ID först
                isValidDepartmentID = validateDepartmentID(departmentElement);

                // Om avdelnings-ID:t är giltigt, fortsätt med ytterligare valideringar
                if (isValidDepartmentID) {
                    isValidDepartmentName = validateDepartmentName(departmentElement);
                    isValidManagerID = validateManagerID(departmentElement);
                    isValidDepartmentLocation = validateDepartmentLocation(departmentElement);
                }

                // Om alla obligatoriska attribut är närvarande och giltiga, skapa och lagra avdelningsobjektet
                if (isValidDepartmentID && isValidDepartmentName && isValidManagerID && isValidDepartmentLocation) {
                    Department department = new Department(departmentID, departmentName, managerID, departmentLocation);
                    departmentArray.add(department);
                    departmentHashMap.put(departmentID, department);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorLogger.logError(e.getMessage()); // Loggar eventuella undantag
        }
    }

    private boolean validateDepartmentID(Element departmentElement) {
        try {
            // Hämta och konverterar avdelnings-ID till int från XML-data
            departmentID = Integer.parseInt(departmentElement.getElementsByTagName("departmentID").item(0).getTextContent());
            int ID = departmentID;

            // Kontrollera om avdelnings-ID är inom det tillåtna intervallet och unikt
            if (ID > 0 && ID < 10000) {
                if (departmentIDs.contains(ID)) {
                    ErrorLogger.logError("Error: Duplicate department ID found: " + ID);
                    return false;
                } else {
                    departmentIDs.add(ID); // Lägg till ID i HashSet om det är unikt
                    return true; // Returnera true om validering lyckades
                }
            } else {
                ErrorLogger.logError("Error: ID is missing or empty for a department.");
                return false;
            }
        } catch (NumberFormatException e) {
            ErrorLogger.logError("Error: ID is not a valid number.");
            return false; // Hoppa över posten om ID inte är en giltig siffra
        }
    }

    private boolean validateDepartmentName(Element departmentElement) {
        // Kontrollera om avdelningens namn finns och inte överstiger 20 tecken
        NodeList nameNodes = departmentElement.getElementsByTagName("name");
        if (nameNodes.getLength() > 0) {
            departmentName = nameNodes.item(0).getTextContent().trim();
            if (departmentName.isEmpty() || departmentName.length() > 20) {
                ErrorLogger.logError("Error: name is missing, too long, or empty for an Department.");
                return false; // Hoppar över post om namnet saknas eller är för långt.
            }
        } else {
            ErrorLogger.logError("Error: name is missing or empty for an department.");
            return false; // Hoppar över post om den är tom eller saknas
        }

        return true;
    }

    private boolean validateManagerID(Element departmentElement) {
        try {
            // Hämta och konverterar Chefs-ID till int från XML-data
            managerID = Integer.parseInt(departmentElement.getElementsByTagName("managerID").item(0).getTextContent());
            int mangerIdentity = managerID;
            if (mangerIdentity > 0 && mangerIdentity < 10000) {
                managerIDs.add(mangerIdentity); // Lägg till ID i HashSet om det är unikt
            } else {
                ErrorLogger.logError("Error: ID is missing or empty for an manager.");
                return false; // Hoppa över post om Chefs-ID saknas eller är tomt
            }
        } catch (NumberFormatException e) {
            ErrorLogger.logError("Error: ID is not a valid number.");
            return false; // Hoppa över post om ID inte är en giltig siffra
        } catch (NullPointerException n) {
            ErrorLogger.logError("Error: managerID is null");
            return false; // Hoppa över post om Chefs-ID är null
        }
        return true;
    }


    private boolean validateDepartmentLocation(Element departmentElement) {
        // Kontrollera om platsen finns och inte överstiger 20 tecken
        NodeList locationNodes = departmentElement.getElementsByTagName("location");
        if (locationNodes.getLength() > 0) {
            departmentLocation = locationNodes.item(0).getTextContent().trim();
            if (departmentLocation.isEmpty() || departmentLocation.length() > 20) {
                ErrorLogger.logError("Error: location is missing, too long, or empty for an Department.");
                return false; // Hoppar över post om platsen saknas eller är för långt.
            }
        } else {
            ErrorLogger.logError("Error: location is missing or empty for a department.");
            return false; // Hoppar över posten om platsen är tom eller saknas
        }
        return true;
    }

}
