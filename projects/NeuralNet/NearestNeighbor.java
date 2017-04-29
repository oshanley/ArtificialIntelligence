  import java.util.*;
  import java.io.*;

  public class NearestNeighbor{
    int K_NEIGHBORS = 3;
    int TRAIN_IMAGES = 10000;
    int TEST_IMAGES = 1000;
    int NUM_PIXELS = 784;
    int NUM_LABELS = 11000;
    double [] distances;
    int [] neighbors;
    double [][] trainImages;
    double [][] testImages;
    int [] labels;

    //description: parses Labels.bin and assigns to array of labels such that labels[i] corresponds to images[i]
    public void parseLabels(){
        int height, width;
        labels = new int [NUM_LABELS];
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
                for (int i=0; i < NUM_LABELS; i++) {
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
            //read each image in
            for (File img : test) {
                //only read in the number of test images
                if(count == TEST_IMAGES){
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
              //calculate the Euclidean distance
              distances[i]+= Math.pow(Math.abs(trainImages[i][p] - pixels[p]),2);
          }
      }
    }

    public void findNeighbors(){
      neighbors = new int [K_NEIGHBORS];
      double min1 = (double)Integer.MAX_VALUE;
      double min2 = (double)Integer.MAX_VALUE;
      double min3 = (double)Integer.MAX_VALUE;
      double[] mins = new double [K_NEIGHBORS];

      Arrays.fill(mins,(double)Integer.MAX_VALUE);

      for(int k = 0; k < K_NEIGHBORS; k++){
          //find mins
          for (int i = 0; i < distances.length; i++) {
              if (k == 0){
                  if (distances[i] < mins[k]){
                      mins[k] = distances[i];
                      neighbors[k] = labels[i];
                  }
              }
              else if (distances[i] < mins[k] && distances[i] > mins[k-1]){
                  mins[k] = distances[i];
                  neighbors[k] = labels[i];
              }
          }
      }
    }

    public int vote(){
        int [] nums = new int[K_NEIGHBORS];
        int decision = 0;

        for (int i = 0; i < neighbors.length; i++) {
            nums[i] = neighbors[i];
        }

        //if all 3 neighbors are the same
        if(nums[0] == nums[1] && nums[0] == nums[2])
            decision = nums[0];
        //if only 1 and 2 are the same
        else if (nums[1] == nums[2])
            decision = nums[1];
        else
            decision = nums[0];

        return decision;
    }

    //check if the decision was correct
    public boolean checkDecision(int decision, int img){
    //   System.out.println("Decision: " + decision + ". Labels[" + (10000+img) +"]: " + labels[10000+img]);

      //offset labels by training images
      if (labels[10000 + img] == decision){
          return true;
      }

      return false;
    }

    public static void main(String[] args) {
        NearestNeighbor nn = new NearestNeighbor();
        int decision = 0;
        double accuracy = 0;
        boolean correct;
        int num_correct = 0 ;

        //parse labels
        nn.parseLabels();

        //parse train images
        nn.parseTrainImages();

        //parse the test images
        nn.parseTestImages();

        //loop through test images
        for (int img = 0; img < nn.TEST_IMAGES; img++) {

            //retrieve the pixels of the test image
            double[] pixels = nn.testImages[img];

            //calculate the distance between the pixels
            nn.calcDistance(pixels);

            //find the closest neighbors to the image
            nn.findNeighbors();

            //vote on the neighbors
            decision = nn.vote();

            //check if the decision was correct
            correct = nn.checkDecision(decision, img);

            if (correct){
              num_correct++;
            }
        }

        accuracy = (double)num_correct/nn.TEST_IMAGES;
        System.out.println("Accuracy: " + accuracy*100 + "%");
  }
}
