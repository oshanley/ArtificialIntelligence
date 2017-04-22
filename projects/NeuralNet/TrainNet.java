import java.util.*;
import java.io.*;

//each image has 784 pixels (0-733)

public class TrainNet{
    int NUM_IMAGES = 500;
    int NUM_PIXELS = 784;
    int NUM_OUTPUTS = 10;
    int  NUM_LABELS = 11000;
    //each of the 10 outputs has a weight for each input pixel plus a bias weight
    int [][] weights = new int [NUM_OUTPUTS][NUM_PIXELS+1];
    //each image has a corresponding label such that the number at image[i] == labels[i]
    int [] labels = new int[NUM_LABELS];
    //each image has an array of outputs and an array of errors used to update the weights
    double [] outputs = new double [NUM_OUTPUTS];
    double [] errors = new double[NUM_OUTPUTS];

    public void parseLabels(){
        int [][] images = new int [NUM_IMAGES][NUM_PIXELS];
        int height, width;

        File labelDir = new File("./labels.bin");

        //for each image, read it in
        try {
    	    FileInputStream fis = new FileInputStream(labelDir);
    	    DataInputStream dis = new DataInputStream(fis);
    	    height = dis.readInt();
    	    width = dis.readInt();

    	    if (width != 1) {
    		System.err.println("Doesn't look like a list ");
    	    }
    	    else{
                //read in each pixel
        	    for (int i=0; i < NUM_LABELS; i++) {
                    labels[i] = dis.readInt();
                    System.out.println("labels[" + i + "]: " + labels[i]);
        	    }
        	    fis.close();
        	    dis.close();
        	}
        }
        catch(IOException e) {
    	    System.err.println("Error reading files ");
    	}
    }

    public int [][] parseImages(){
        int [][] images = new int [NUM_IMAGES][NUM_PIXELS];
        int height, width;
        int count = 0;

        File trainDir = new File("./train_images_" + NUM_IMAGES);
        File[] trainImages = trainDir.listFiles();

        //read from the train_images folder
        if (trainImages != null) {
            //for each image, read it in
            for (File img : trainImages) {
                try {
            	    FileInputStream fis = new FileInputStream(img);
            	    DataInputStream dis = new DataInputStream(fis);
            	    height = dis.readInt();
            	    width = dis.readInt();

                    //read in each pixel
            	    for (int i=0; i < NUM_PIXELS; i++) {
                        //divide pixel values by 255 to normalize to 0 or 1
                        images[count][i] = dis.readInt()/255;
                        //System.out.println("images[" + count + "][" + i + "]: " + images[count][i]);
            	    }
            	    fis.close();
            	    dis.close();
                    count++;
            	}
                catch(IOException e) {
            	    System.err.println("Error reading files ");
            	}
            }
        }
        else {
            System.out.print("ERROR: Unable to read from directory");
        }

        return images;
    }

    //input: array of pixels of an image
    //output: the decision of the network
    //description: computes the output weights for a given image and selects the one
    //             with the highest value as the answer to the input
    public int computeOutputs(int [] pixels){
        int in, answer = 0;
        double g, max = 0;

        //calculate the output weights for a given image
        for (int i = 0; i < NUM_OUTPUTS; i++) {
            in = 0;
            g = 0;

            for (int j = 0; j < NUM_PIXELS; j++) {
                in += weights[i][j]*pixels[j];
            }

            g = 1/(1 + Math.exp(-1*in));
            //store to array of outputs
            outputs[i] = g;
            System.out.println("Outputs["+ i +"]: " + g);
        }

        //the output with the greatest weight is the decision of the network
        for (int i = 0; i < NUM_OUTPUTS; i++) {
            if(outputs[i] > max){
                max = outputs[i];
                answer = i;
            }
        }

        //return the decision
        System.out.println("Decision: " + answer);
        return answer;
    }

    //input: decision of the network; correct label; number of output weight
    //description: computes the error of a given output
    public void computeError(int output, int label, int num){
        double error, y;

        //if the network chose the right label, let y =1
        if(output == num && label == output){
            y = 1;
            //System.out.println("Correct");
        }
        else{
            y = 0;
            //System.out.println("Wrong");
        }

        //calculate the error
        errors[num] = y - outputs[num];
    }

    public static void main(String[] args) {
        TrainNet train = new TrainNet();
        int [][] images;
        int output;

        //read from train directory and fill images[][] with pixels
        System.out.println("===== Reading in images. This may take some time. =====");
        images = train.parseImages();
        System.out.println("===== Finished reading images =====");
        //parse labels
        train.parseLabels();

        // for(int img = 0; img < train.NUM_IMAGES; img++){
        //     for(int pixel = 0; pixel < 784; pixel++){
        //         System.out.println("images[" + img + "][" + pixel + "]: " + images[img][pixel]);
        //     }
        // }

        //11000 images, 11000 labels. labels[i] == output of network


        //for every image, train the network
        for(int img = 0; img < train.NUM_IMAGES; img++){
            //compute output weights
            output = train.computeOutputs(images[img]);
            //System.out.println("Labels[" + img + "]:" + train.labels[img]);
            if(output == train.labels[img]){
                System.out.println("Correct decision");
            }
            else
                System.out.println("Wrong");

            //for each output, compute the error
            for(int num = 0; num < train.NUM_OUTPUTS; num++){
                train.computeError(output, train.labels[img], num);
                System.out.println("Errors[" + num + "]: " + train.errors[num]);
            }
        }
    }
}
