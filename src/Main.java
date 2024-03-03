import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "src/Assets/NordicPeakEmployees.xml";
        XmlReader xmlReader = new XmlReader(filePath);
        xmlReader.parseAndValidateXML();

    }
}