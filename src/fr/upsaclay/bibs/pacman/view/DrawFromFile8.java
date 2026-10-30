package fr.upsaclay.bibs.pacman.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static java.lang.Integer.valueOf;

public class DrawFromFile8 {

    private static Color[][] colorPixelTable24;

    public DrawFromFile8(String fileName, Color[] colorList) throws IOException, URISyntaxException {

        try {

            URL url = getClass().getClassLoader().getResource("tiles/" + fileName);
            List<String> lines;

            if (url != null) {

                File file = new File(url.toURI());
                lines = Files.readAllLines(file.toPath());
                Color[][] colorPixelTable8 = new Color[8][8];

                for (int i = 0; i < 8; i++) {
                    String[] pixelsLine = lines.get(i).split(" ");
                    for (int j = 0; j < 8; j++) {
                        colorPixelTable8[i][j] = colorList[Integer.parseInt(pixelsLine[j])];
                    }
                }

                colorPixelTable24 = triplingColorMatrix(colorPixelTable8);

            } else {
                System.out.println("File not found: " + fileName);
            }


        } catch (IOException | URISyntaxException e) {
            throw new IOException("Erreur lors du chargement du fichier : " + e.getMessage());
        }
    }

    public static Color[][] triplingColorMatrix(Color[][] colorPixelTable8){

        Color[][] colorPixelTable24 = new Color[24][24];

        for (int i = 0; i < 24; i++) {

            for (int j = 0; j < 24; j++) {

                //System.out.println(i/3 + " ; " + j/3);

                colorPixelTable24[i][j] = colorPixelTable8[i/3][j/3];

            }
        }

        return colorPixelTable24;
    }

    public Color[][] getColorPixelTable24(){
        return colorPixelTable24;
    }
}