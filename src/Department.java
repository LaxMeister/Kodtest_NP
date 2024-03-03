public class Department {
    private int departmentID;
    private String name;
    private int managerID;
    private String location;

    public Department(int departmentID, String name, int managerID, String location) {
        this.departmentID = departmentID;
        this.name = name;
        this.managerID = managerID;
        this.location = location;
    }

    // Getters for department attributes
    public int getDepartmentID() {
        return departmentID;
    }

    public String getName() {
        return name;
    }

    public int getManagerID() {
        return managerID;
    }

    public String getLocation() {
        return location;
    }
}