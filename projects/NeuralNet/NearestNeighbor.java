import java.util.*;
import java.io.*;

public class NearestNeighbor{
  int K_NEIGHBORS = 3; //k is always odd to avoid ties
  int TRAIN_IMAGES = 100;
  int NUM_PIXELS = 784;
  int TEST_IMAGES = 1;
  double [] distances;
  int [] neighbors;
  double [][] trainImages;
  double [][] testImages;
  int [] labels;

  //description: parses Labels.bin and assigns to array of labels such that labels[i] corresponds to images[i]
  public void parseLabels(){
      int height, width;
      labels = new int [TRAIN_IMAGES];
      File labelDir = new File("./labels.bin");

      //for each image, read in each pixel
      try {
          FileInputStream fis = new FileInputStream(labelDir);
          DataInputStream dis = new DataInputStream(fis);
          height = dis.readInt();
          width = dis.readInt();

          if (width != 1) {
          System.err.println("Doesn't look like a list ");
          }
          else{
              for (int i=0; i < TRAIN_IMAGES; i++) {
                  labels[i] = dis.readInt();
              }
              fis.close();
              dis.close();
          }
      }
      catch(IOException e) {
          System.err.println("Error reading files ");
      }
  }

  //description: for every image, parses and stores pixels normalized to values between 0 and 1
  public void parseTestImages(){
      testImages = new double [TEST_IMAGES][NUM_PIXELS];
      int height, width;
      int count = 0;

      File testDir = new File("./test_images");
      File[] test = testDir.listFiles();

      //read from the train_images folder
      if (testImages != null) {
              //for each image, read it in
              for (File img : test) {
                  if(count == TEST_IMAGES){
                      // System.out.println("BREAK. Num imgs: " + images.length);
                      break;
                  }
              try {
                  FileInputStream fis = new FileInputStream(img);
                  DataInputStream dis = new DataInputStream(fis);

                  //read dimensions from file
                  height = dis.readInt();
                  width = dis.readInt();

                  //read in each pixel
                  for (int i=0; i < NUM_PIXELS; i++) {
                      //divide pixel values by 255 to normalize between 0 or 1
                      testImages[count][i] = ((double)dis.readInt())/255;
                    //   System.out.println("testImages[" + count + "][" + i + "]: " + testImages[count][i]);

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
  }

  //description: for every image, parses and stores pixels normalized to values between 0 and 1
  public void parseTrainImages(){
      trainImages = new double [TRAIN_IMAGES][NUM_PIXELS];
      int height, width;
      int count = 0;

      File trainDir = new File("./train_images");
      File[] train = trainDir.listFiles();

      //read from the train_images folder
      if (trainImages != null) {
              //for each image, read it in
              for (File img : train) {
                  if(count == TRAIN_IMAGES){
                      // System.out.println("BREAK. Num imgs: " + images.length);
                      break;
                  }
              try {
                  FileInputStream fis = new FileInputStream(img);
                  DataInputStream dis = new DataInputStream(fis);

                  //read dimensions from file
                  height = dis.readInt();
                  width = dis.readInt();

                  //read in each pixel
                  for (int i=0; i < NUM_PIXELS; i++) {
                    //divide pixel values by 255 to normalize between 0 or 1
                      trainImages[count][i] = ((double)dis.readInt())/255;
                    //   System.out.println("trainImages[" + count + "][" + i + "]: " + trainImages[count][i]);

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
  }

  //Euclidean Distance, p = 2; sqrt not necessary
  public void calcDistance(double [] pixels){
      int x = 0;
      int j = 0;
      int q = 0;

      //initialize array of euclidean distances between test and train pixels
      distances = new double [TRAIN_IMAGES];

      //loop through all training images
      for (int i = 0; i < TRAIN_IMAGES; i++) {
          for (int p = 0; p < NUM_PIXELS; p++) {
              distances[i]+= Math.pow(Math.abs(trainImages[i][p] - pixels[p]),2);
            //  System.out.println("distances[" + i + "]+= Math.pow(Math.abs(trainImages[" + i + "]["+ p + "] - pixels[" + p + "]),2)");
          }
          System.out.println("distances[" + i + "] = " + distances[i]);
      }
  }

  public void findNeighbors(){
      neighbors = new int [K_NEIGHBORS];
      double min1 = (double)Integer.MAX_VALUE;
      double min2 = (double)Integer.MAX_VALUE;
      double min3 = (double)Integer.MAX_VALUE;

      /*
      for(int k = 0; k < K_NEIGHBORS; k++){
          //find mins
          for (int i = 0; i < distances.length; i++) {
              if (k == 0){
                  if (distances[i] < mins[k]){
                      mins[k] = distances[i];
                      neighbors[k] = i;
                  }
              }
              else if (distances[i] < mins[k+1] && distances[i] > mins[k]){
                  mins[k] = distances[i];
                  neighbors[k] = i;
              }
          }
          System.out.println("Mins["+k+"]: " + mins[k]);
      }
      */

      //find min1
      for (int i = 0; i < distances.length; i++) {
          if (distances[i] < min1){
              min1 = distances[i];
              neighbors[0] = i;
          }
      }

      //find min2
      for (int i = 0; i < distances.length; i++) {
          if (distances[i] < min2 && distances[i] > min1){
              min2 = distances[i];
              neighbors[1] = i;
          }
      }

       //find min3
       for (int i = 0; i < distances.length; i++) {
           if (distances[i] < min3 && distances[i] > min2){
               min3 = distances[i];
               neighbors[2] = i;
           }
       }

      System.out.println("Min1 found at distances[" + neighbors[0] + "]: " + distances[neighbors[0]]);
      System.out.println("Min2 found at distances[" + neighbors[1] + "]: " + distances[neighbors[1]]);
      System.out.println("Min3 found at distances[" + neighbors[2] + "]: " + distances[neighbors[2]]);

  }

    public void vote(){
        int [] nums = new int[K_NEIGHBORS];
        int vote = 0;

        for (int i = 0; i < neighbors.length; i++) {
            nums[i] = labels[neighbors[i]];
            System.out.println("neighbors["+i+"]: " + nums[i]);
        }

        //if all 3 neighbors are the same
        if(nums[0] == nums[1] && nums[0] == nums[2])
            vote = nums[0];
        //if only 1 and 2 are the same
        else if (nums[1] == nums[2])
            vote = nums[1];
        else 
            vote = nums[0];

        System.out.println("Decision: " + vote);
    }

    public static void main(String[] args) {
        NearestNeighbor nn = new NearestNeighbor();

        nn.parseLabels();

      //parse images
      nn.parseTrainImages();

      //parse the train images
      nn.parseTestImages();

      for (int img = 0; img < nn.TEST_IMAGES; img++) {

          //store the pixels of the test image
          double[] pixels = nn.testImages[img];
          nn.calcDistance(pixels);
          nn.findNeighbors();
          nn.vote();
      }
  }
}
