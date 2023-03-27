import gauss.Algorithm;
import gauss.MyEquation;
import gauss.LinearSystem;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static final int DEFAULT_EQUATIONS_NUMBER = 3;
    private static final int DEFAULT_VARIABLES_NUMBER = 3;

    public static void main(String args[]) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Choose your option: To generate data - 1;  To read data from file - 2");
        int option = in.nextInt();

        LinearSystem<Float, MyEquation> list = generateSystem(option);
        printSystem(list);
        printSystemToFile(list);
        int i, j;
        long time = System.currentTimeMillis();

        Algorithm<Float, MyEquation> alg = new Algorithm<Float, MyEquation>(list);
        try{
            alg.calculate();
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }catch (ArithmeticException e){
            System.out.println(e.getMessage());
            System.exit(0);
        }
        Float [] x = new Float[DEFAULT_EQUATIONS_NUMBER];
        for(i = list.size() - 1; i >= 0; i--) {
            Float sum = 0.0f;
            for(j = list.size() - 1; j > i; j--) {
                sum += list.itemAt(i, j) * x[j];
            }
            x[i] = (list.itemAt(i, list.size()) - sum) / list.itemAt(i, j);

        }
        System.out.println("Ñomputation time: "+(System.currentTimeMillis() - time)+" milliseconds");



        System.out.println("Show answer on the screen - 1 or write to file - 2");
        int answer = in.nextInt();
        in.close();
        if(answer==1)
            printVector(x);
        else
            printVectorToFile(x);

        in.close();

    }

    public static LinearSystem<Float, MyEquation> generateSystem(int option) throws IOException {


        FileReader file = new FileReader("data_to_read.txt");

        Scanner scan = new Scanner(file);

        if(option==1) {
            LinearSystem<Float, MyEquation> list = new LinearSystem<Float, MyEquation>();
            int i;
            for (i = 0; i < DEFAULT_EQUATIONS_NUMBER; i++){
                MyEquation eq = new MyEquation();
                eq.generate(DEFAULT_VARIABLES_NUMBER + 1);
                list.push(eq);
            }
            return list;

        }
        else  {
            LinearSystem<Float, MyEquation> list = new LinearSystem<Float, MyEquation>();
            int i;
            for (i = 0; i < DEFAULT_EQUATIONS_NUMBER; i++){
                MyEquation eq = new MyEquation();
                for (int k = 0; k < DEFAULT_EQUATIONS_NUMBER+1; k++){

                    eq.equation.add((float) (scan.nextInt()));
                }
                list.push(eq);
            }
            return list;
        }


    }


    public static void printSystem(LinearSystem<Float, MyEquation> system){
        for (int i = 0; i < system.size(); i++){
            MyEquation temp = system.get(i);
            String s = "";
            for (int j = 0; j < temp.size(); j++){
                s += String.format("%f; %s", system.itemAt(i, j), "\t");
            }
            System.out.println(s);
        }System.out.println("");
    }

    public static void printSystemToFile(LinearSystem<Float, MyEquation> system) throws FileNotFoundException {
        File file = new File("created_data.txt");
        PrintWriter pw = new PrintWriter(file);



        for (int i = 0; i < system.size(); i++){
            MyEquation temp = system.get(i);
            String s = "";
            for (int j = 0; j < temp.size(); j++){
                s += String.format("%f; %s", system.itemAt(i, j), "\t");
            }
            pw.println(s);
        }pw.println("");


        pw.close();
    }

    public static void printVector(Float [] x){
        String s = "";
        for (int i = 0; i < x.length; i++){
            s += String.format("x%d = %f; ", i, x[i]);
        }System.out.println(s);
    }

    public static void printVectorToFile(Float [] x) throws FileNotFoundException {

        File file = new File("result.txt");
        PrintWriter pw = new PrintWriter(file);

        String s = "";
        for (int i = 0; i < x.length; i++){
            s += String.format("x%d = %f; ", i, x[i]);
        }pw.println(s);

        pw.close();

        System.out.println("Answer was written successfully.");

    }
}
