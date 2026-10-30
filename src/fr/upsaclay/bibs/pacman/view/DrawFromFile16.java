package fr.upsaclay.bibs.pacman.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class DrawFromFile16 {

    private final Color[][] colorPixelTable24 = new Color[24][24];

    public DrawFromFile16(String fileName, Color[] colorList) throws IOException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource("ghosts/ghosts24/" + fileName);

        if (url == null) {
            throw new IOException("File not found: " + fileName);
        }

        File file = new File(url.toURI());
        List<String> lines = Files.readAllLines(file.toPath());


        for (int i = 0; i < 24; i++) {
            String[] pixelsLine = lines.get(i).split(" ");

            for (int j = 0; j < 24; j++) {
                //System.out.println(i + " ; " + j + " : " + Integer.parseInt(pixelsLine[j])); VERIF OK
                int index = Integer.parseInt(pixelsLine[j]);
                if (index < 0 || index >= colorList.length) {
                    throw new IOException("Invalid color index: " + index + " at (" + i + "," + j + ")");
                }
                //System.out.println(i + " ; " + j + " : " + colorList[index]);  VERIF OK
                colorPixelTable24[i][j] = colorList[index];
                //System.out.println(i + " ; " + j + " : " + colorPixelTable24[i][j]);  VERIF OK
            }
        }
    }

    public Color[][] getColorPixelTable24() {
        //System.out.println(colorPixelTable24[4][3]);
        return colorPixelTable24;
    }
}
