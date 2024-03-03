import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FinalValidation {

    // Listor för att lagra validerade anställda och avdelningar
    public List<Department> validDepartments;
    public List<Employee> validEmployees;

    // Konstruktorer
    public FinalValidation(List<Employee> validEmployees, List<Department> validDepartments) {
        this.validEmployees = validEmployees;
        this.validDepartments = validDepartments;
    }

    public FinalValidation() {
    }

    // Metod för att utföra slutlig validering
    public FinalValidation finalValidation(HashMap<Integer, Employee> employeeHashMap, HashMap<Integer, Department> departmentHashMap) {

        // Skapa HashMap för avdelningar med avdelnings-ID som nyckel
        HashMap<Integer, Department> departmentMap = (HashMap<Integer, Department>) departmentHashMap.values().stream()
                .collect(Collectors.toMap(Department::getDepartmentID, Function.identity()));

        // Använd stream för att omvandla värden i HashMap till ny HashMap med avdelnings-ID som nyckel
        // Function.identity() behåller värdet oförändrat

        // Skapa HashMap för anställda med anställnings-ID som nyckel
        HashMap<Integer, Employee> employeeMap = (HashMap<Integer, Employee>) employeeHashMap.values().stream()
                .collect(Collectors.toMap(Employee::getEmployeeID, Function.identity()));

        // Använd ström för att omvandla värden i HashMap till ny HashMap med anställnings-ID som nyckel

        // Validera anställda: filtrera bort anställda med ogiltigt avdelnings-ID
        validEmployees = employeeMap.values().stream()
                .filter(employee -> departmentMap.containsKey(employee.getDepartmentID())) // Kolla om avdelnings-ID finns i departmentMap
                .collect(Collectors.toList());

        // Validera chefer: filtrera bort anställda med ogiltigt chef-ID
        validEmployees = validEmployees.stream()
                .filter(employee -> {
                    int managerID = departmentMap.get(employee.getDepartmentID()).getManagerID();
                    return employeeMap.containsKey(managerID);
                })
                .collect(Collectors.toList());

        // Samla giltiga avdelningar baserat på de validerade anställdas avdelningar
        validDepartments = validEmployees.stream()
                .map(employee -> departmentMap.get(employee.getDepartmentID())) // Hämta avdelning för varje anställd
                .filter(Objects::nonNull) // Filtrera bort null-värden
                .distinct() // Ta bort duplicerade avdelningar
                .collect(Collectors.toList());

        // Returnera en ny instans av FinalValidation med validerade listor
        return new FinalValidation(validEmployees, validDepartments);
    }

    // Getter och setter metoder för validDepartments och validEmployees
    public List<Department> getValidDepartments() {
        return validDepartments;
    }

    public void setValidDepartments(List<Department> validDepartments) {
        this.validDepartments = validDepartments;
    }

    public List<Employee> getValidEmployees() {
        return validEmployees;
    }

    public void setValidEmployees(List<Employee> validEmployees) {
        this.validEmployees = validEmployees;
    }
}
