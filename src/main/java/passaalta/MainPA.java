/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passaalta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author rmachado
 */
public class MainPA {
    public static void main(String[] args) throws Exception {
        String nome = "Lena"; //Lena OU Monalisa
        File file = new File(nome+".pgm");
        File newFile = new File(nome+"PassaAlta.pgm");
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
        
        int[][] paMatrix = {{-1, -1, -1},
                            {-1, 8, -1},
                            {-1, -1, -1}};
        
        String st;
        int count = 0, width = 0, height = 0, maxGrayValue = 0;
        ArrayList<Integer> pixels = new ArrayList();
        while ((st = br.readLine()) != null) {
            if (count == 0) { //magic number keeps the same
                bw.write(st);
                bw.append("\n");
            }
            if (count == 1) {
                bw.append("# "+nome+"PassaAlta.pgm created by Rafael Machado\n");
            }
            if (count == 2) { //image dimension keeps the same
                String[] dim = st.split(" ");
                width = Integer.parseInt(dim[0]);
                height = Integer.parseInt(dim[1]);
                
                System.out.println("Image size: "+width+"x"+height);
                
                bw.append(st+"\n");
            }
            if (count == 3) { //the maximum gray value keeps the same
                maxGrayValue = Integer.parseInt(st);
                bw.append(st+"\n");
            }
            if (count >= 4) { //image body
                String[] line = st.split(" ");
                for (String pixel : line) {
                    if (!pixel.isBlank()) {
                        pixels.add(Integer.parseInt(pixel));
                    }
                }
            }
            count++;
        }
        
        int[][] img = new int[height + 2][width + 2];
        
        //add borders with zeros
        for (int j = 0; j < width + 2; j++) {
            img[0][j] = 0;
            img[height + 1][j] = 0;
        }
        
        //get image pixels with the right dimensions (also adding 0 to the beginning and end of the row)
        ArrayList<Integer> row = new ArrayList();
        int cont = 1;
        for (int i = 0; i < height*width; i = i + width) {
            row.add(0);
            for (int j = 0; j < width; j++) {
                row.add(pixels.get(i+j));
            }
            row.add(0);
            for (int j = 0; j < row.size(); j++) {
                img[cont][j] = row.get(j);
            }
            cont++;
            row.clear();
        }
        
        //create the new image with filter
        int[][] img_passaalta = new int[height][width];
        for (int i = 1; i < height + 1; i++) {
            for (int j = 1; j < width + 1; j++) {
                img_passaalta[i - 1][j - 1] = (img[i-1][j-1]*paMatrix[0][0] +
                                               img[i-1][j]*paMatrix[0][1] +
                                               img[i-1][j+1]*paMatrix[0][2] +
                                               img[i][j-1]*paMatrix[1][0] +
                                               img[i][j]*paMatrix[1][1] +
                                               img[i][j+1]*paMatrix[1][2] +
                                               img[i+1][j-1]*paMatrix[2][0] +
                                               img[i+1][j]*paMatrix[2][1] +
                                               img[i+1][j+1]*paMatrix[2][2]);
                
                //if pixel is negative, then set it to 0
                if (img_passaalta[i-1][j-1] < 0) {
                    img_passaalta[i-1][j-1] = 0;
                }
                //if pixel is bigger than the maximum value (e.g. 255), then set it to the maximum value
                if (img_passaalta[i-1][j-1] > maxGrayValue) {
                    img_passaalta[i-1][j-1] = maxGrayValue;
                }
                
                bw.append(String.valueOf(img_passaalta[i-1][j-1]) + " ");
            }
            bw.append("\n");
        }
        
        bw.close();
        
        System.out.println("PASSA ALTA: DONE!");
    }
}
