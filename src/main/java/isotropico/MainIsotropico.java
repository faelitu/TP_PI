/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isotropico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author rmachado
 */
public class MainIsotropico {
    public static void main(String[] args) throws Exception {
        String nome = "Monalisa"; //Lena OU Monalisa
        File file = new File(nome+".pgm");
        File newFile = new File(nome+"Isotropico.pgm");
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
        
        double[][] XMatrix = {{-1, -sqrt(2), -1},
                           {0, 0, 0},
                           {1, sqrt(2), 1}};
        
        double[][] YMatrix = {{-1, 0, 1},
                           {-sqrt(2), 0, sqrt(2)},
                           {-1, 0, 1}};
        
        String st;
        int count = 0, width = 0, height = 0, maxGrayValue = 0;
        ArrayList<Integer> pixels = new ArrayList();
        while ((st = br.readLine()) != null) {
            if (count == 0) { //magic number keeps the same
                bw.write(st);
                bw.append("\n");
            }
            if (count == 1) {
                bw.append("# "+nome+"Isotropico.pgm created by Rafael Machado\n");
            }
            if (count == 2) { //image dimension keeps the same
                String[] dim = st.split(" ");
                width = Integer.parseInt(dim[0]);
                height = Integer.parseInt(dim[1]);
                
                System.out.println("Image size: "+height+"x"+width);
                
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
        int[][] img_isotropico = new int[height][width];
        for (int i = 1; i < height + 1; i++) {
            for (int j = 1; j < width + 1; j++) {
                double gradX = (img[i-1][j-1]*XMatrix[0][0] +
                               img[i-1][j]*XMatrix[0][1] +
                               img[i-1][j+1]*XMatrix[0][2] +
                               img[i][j-1]*XMatrix[1][0] +
                               img[i][j]*XMatrix[1][1] +
                               img[i][j+1]*XMatrix[1][2] +
                               img[i+1][j-1]*XMatrix[2][0] +
                               img[i+1][j]*XMatrix[2][1] +
                               img[i+1][j+1]*XMatrix[2][2]);
                
                double gradY = (img[i-1][j-1]*YMatrix[0][0] +
                               img[i-1][j]*YMatrix[0][1] +
                               img[i-1][j+1]*YMatrix[0][2] +
                               img[i][j-1]*YMatrix[1][0] +
                               img[i][j]*YMatrix[1][1] +
                               img[i][j+1]*YMatrix[1][2] +
                               img[i+1][j-1]*YMatrix[2][0] +
                               img[i+1][j]*YMatrix[2][1] +
                               img[i+1][j+1]*YMatrix[2][2]);
                
                img_isotropico[i-1][j-1] = (int) round(sqrt(gradX*gradX + gradY*gradY));
                
                bw.append(String.valueOf(img_isotropico[i-1][j-1]) + " ");
            }
            bw.append("\n");
        }
        
        bw.close();
        
        System.out.println("ISOTROPICO: DONE!");
    }
}
