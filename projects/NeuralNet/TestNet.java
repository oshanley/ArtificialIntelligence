import java.util.*;
import java.io.*;

public class TestNet{
    double [][] weights;

    public void readWeights(){
        File weightsDir = new File("./weights.bin");
        int rows = 0;
        int cols = 0;
        //for each image, read in each pixel
        try {
            FileInputStream fis = new FileInputStream(weightsDir);
            DataInputStream dis = new DataInputStream(fis);

            //read dimensions
            rows = dis.readInt();
            cols = dis.readInt();

            System.out.println("rows: " + rows + " cols: " + cols);

            //initialize weights array
            weights = new double[rows][cols];

            //read weights
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols ; j++) {
                    weights[i][j] = dis.readDouble();
                    // System.out.println("Reading weights[" + i + "][" + j + "]: " + weights[i][j]);
                }
            }

            fis.close();
            dis.close();
        }
        catch(IOException e) {
            System.err.println("Error reading files ");
        }
    }


    public static void main(String[] args) {
        TestNet test = new TestNet();

        test.readWeights();
    }
}
