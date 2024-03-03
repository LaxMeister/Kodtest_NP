import Utils.ErrorLogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class CreatePassCard extends JPanel {
    private BufferedImage passCardImage;
    private BufferedImage profImage;
    private String firstName;
    private String lastname;
    private String department;
    private String location;

    public CreatePassCard(String profileImage,String firstName, String lastName,String department, String location) {
        this.firstName = firstName;
        this.lastname = lastName;
        this.department = department;
        this.location = location;
        try {
            // Decode:a strängen till en byte-array.
            byte[] decodedBytes = Base64.getDecoder().decode(profileImage);

            // Skapa en bild från byte-arrayen.
            ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
            BufferedImage image = ImageIO.read(bis);
            bis.close();

            // Spara bilden som en fil.
            File outputFile = new File("profile_image.png"); // Ange filnamnet och filtypen du vill spara bilden som
            ImageIO.write(image, "png", outputFile);

            System.out.println("Bilden har sparats som " + outputFile.getName());
        } catch (IOException e) {
            System.out.println("Något gick fel: " + e.getMessage());
        }
        try {
            // Läs in mallbilden och profilbilden
            passCardImage = ImageIO.read(new File("PasserkortMall.png")); // Ange sökvägen till din mallbild här
            profImage = ImageIO.read(new File("profile_image.png")); // Ange sökvägen till din mallbild här

        } catch (IOException e) {
            e.printStackTrace();
            ErrorLogger.logError(e.getMessage());
        }

        // Sätt storleken på panelen baserat på mallbilden
        setPreferredSize(new Dimension(passCardImage.getWidth(), passCardImage.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Rita mallbilden
        g2d.drawImage(passCardImage, 0, 0, this);

        // Justera storleken på profilbilden
        int profileImageWidth = 300; // Ange önskad bredd för profilbilden
        int profileImageHeight = 330; // Ange önskad höjd för profilbilden

        // Placera och rita profilbilden med justerad storlek
        int profileImageX = 150; // Justera X-koordinaten för profilbilden enligt din layout
        int profileImageY = 100; // Justera Y-koordinaten för profilbilden enligt din layout
        g2d.drawImage(profImage, profileImageX, profileImageY, profileImageWidth, profileImageHeight, this);


        // Rita förnamnet
        int nameX = 150; // Justera X-koordinaten för namnet enligt din layout
        int nameY = 530; // Justera Y-koordinaten för namnet enligt din layout
        g2d.setColor(Color.BLACK); // Ange färgen för texten
        g2d.setFont(new Font("Arial", Font.PLAIN, 22)); // Ange typsnitt och storlek för texten
        g2d.drawString(firstName, nameX, nameY); // Rita ut namnet

        // Rita efternamnet
        int lnameX = 150; // Justera X-koordinaten för namnet enligt din layout
        int lnameY = 600; // Justera Y-koordinaten för namnet enligt din layout
        g2d.setColor(Color.BLACK); // Ange färgen för texten
        g2d.setFont(new Font("Arial", Font.PLAIN, 22)); // Ange typsnitt och storlek för texten
        g2d.drawString(lastname, lnameX, lnameY); // Rita ut namnet

        // Rita avdelningen
        int departmentX = 150; // Justera X-koordinaten för namnet enligt din layout
        int departmentY = 675; // Justera Y-koordinaten för namnet enligt din layout
        g2d.setColor(Color.BLACK); // Ange färgen för texten
        g2d.setFont(new Font("Arial", Font.PLAIN, 22)); // Ange typsnitt och storlek för texten
        g2d.drawString(department, departmentX, departmentY); // Rita ut namnet

        // Rita plats
        int locationX = 150; // Justera X-koordinaten för namnet enligt din layout
        int locationY = 750; // Justera Y-koordinaten för namnet enligt din layout
        g2d.setColor(Color.BLACK); // Ange färgen för texten
        g2d.setFont(new Font("Arial", Font.PLAIN, 22)); // Ange typsnitt och storlek för texten
        g2d.drawString(location, locationX, locationY); // Rita ut namnet
    }
}
