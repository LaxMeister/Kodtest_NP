public class Employee {
    private int employeeID;
    private String firstName;
    private String lastName;
    private String citizenIdentifier;
    private String employmentDate;
    private int departmentID;
    private String profileImage;

    public Employee(int employeeID, String firstName, String lastName, String citizenIdentifier,
                    String employmentDate, int departmentID, String profileImage) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenIdentifier = citizenIdentifier;
        this.employmentDate = employmentDate;
        this.departmentID = departmentID;
        this.profileImage = profileImage;
    }

    // Getters for employee attributes
    public int getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCitizenIdentifier() {
        return citizenIdentifier;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public String getProfileImage() {
        return profileImage;
    }
}