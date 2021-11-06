package Lab3;

import java.math.BigDecimal;
import java.util.Random;

public class Iteration_method {

    private static Integer matrix_A[][];
    private static Integer matrix_X[];
    private static Integer matrix_f[];
    private static Integer matrix_x0[];
    private static Double matrix_xq[];
    private static Double rel_c[] = {0.2, 0.5, 0.8, 1.0, 1.3, 1.5, 1.8};
    private static BigDecimal epsilon = new BigDecimal(Math.pow(10,-7));
    private static Integer max_k = 5000;
    private static Integer it_num = 0;
    private static Double error = 0.0;

    public static void fill_matrix(int size, int param){
        Random elem = new Random();
        Double sum = 0.0;
        matrix_A = new Integer[size][size];
        matrix_X = new Integer[size];
        matrix_x0 = new Integer[size];
        for(int i = 0; i < size; i++){
            for (int j = i + 1; j < size; j++) {
                matrix_A[i][j] = (int)(Math.pow(-1.0, elem.nextInt()) * elem.nextInt(100));
                matrix_A[j][i] = matrix_A[i][j];
            }
        }
        for (int i = 0; i < size; i++) {
            for (Integer temp: matrix_A[i]) {
                if(temp != null)
                sum += Math.abs(temp);
            }
            matrix_A[i][i] = elem.nextInt((int)(sum + param), (int)(sum + 10 * param));
            matrix_X[i] = i + 1;
            matrix_x0[i] = 0;
        }
    }

    public static void fill_f(){
        Integer temp = 0;
        matrix_f = new Integer[matrix_A.length];
        for (int i = 0; i < matrix_A.length; i++) {
            for (int j = 0; j < matrix_A.length; j++) {
                    temp += matrix_A[i][j] * matrix_X[j];
            }
            matrix_f[i] = temp;
            temp = 0;
        }
    }

    public static void print(){
        System.out.println("Matrix A: ");
        for (Integer[] row: matrix_A) {
            for (Integer column: row) {
                System.out.printf("%-5s", column);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("\n"+"Matrix X: ");
        for (Integer temp: matrix_X)
            System.out.printf("%-4s",temp);
        System.out.println("\n\n"+"Matrix xq: ");
        for (Double temp: matrix_xq)
            System.out.printf("%-20s",temp);
        System.out.println("\n\n"+"Matrix f: ");
        for (Integer temp: matrix_f)
            System.out.printf("%-6s",temp);
        System.out.println("\n\n"+"Matrix x0: ");
        for (Integer temp: matrix_x0)
            System.out.printf("%-4s", temp);
        System.out.println("\n\n"+"Number of iterations: "+"\n"+it_num);
        System.out.println("\n"+"Error: "+"\n"+error);
    }

    public static Double norm(Double[] xK){
        Double temp = 0.0, norm = 0.0;
        Double temp_matrix[] = new Double[matrix_A.length];
        for (int i = 0; i < matrix_A.length; i++) {
            for (int j = 0; j < matrix_A.length; j++) {
                temp += matrix_A[i][j] * xK[j];
            }
            temp_matrix[i] = temp;
            temp = 0.0;
        }
        for (int i = 0; i < temp_matrix.length; i++)
            temp_matrix[i] = temp_matrix[i] - matrix_f[i];
        for (Double tmp: temp_matrix)
            norm += Math.abs(tmp);
        return norm;
    }

    public static void grad(){
        Double xK[] = new Double[matrix_A.length];
        Double rK[] = new Double[matrix_A.length];
        Double temp_matrix[] = new Double[matrix_A.length];
        Double tauK, temp_u, temp_l;
        int k = 0;
        for (int i = 0; i < xK.length; i++)
            xK[i] = (double)matrix_x0[i];
        while(k < max_k && epsilon.compareTo(new BigDecimal(norm(xK))) < 0){
            k++;
            Double temp = 0.0;
            temp_u = 0.0;
            temp_l = 0.0;
            //rK
            for (int i = 0; i < matrix_A.length; i++) {
                for (int j = 0; j < matrix_A.length; j++) {
                    temp += matrix_A[i][j] * xK[j];
                }
                rK[i] = temp;
                temp = 0.0;
            }
            for (int i = 0; i < rK.length; i++)
                rK[i] = rK[i] - matrix_f[i];
            //rK*rK
            for (Double tmp: rK)
               temp_u += tmp*tmp;
            //ArK*rK
            for (int i = 0; i < matrix_A.length; i++) {
                for (int j = 0; j < matrix_A.length; j++) {
                    temp += matrix_A[i][j] * rK[j];
                }
                temp_matrix[i] = temp;
                temp = 0.0;
            }
            for (int i = 0; i < rK.length; i++)
                temp_l += temp_matrix[i]*rK[i];
            //tauK
            tauK = temp_u/temp_l;
            //x(K+1)
            for (int i = 0; i < xK.length; i++)
                xK[i] = xK[i] - tauK*rK[i];
        }
        it_num = k;
        matrix_xq = xK;
        for (int i = 0; i < matrix_xq.length; i++)
            error += Math.abs(matrix_xq[i] - matrix_X[i]);
    }

    public static void relax(Double rel_c){
        Double xK[] = new Double[matrix_A.length];
        for (int i = 0; i < xK.length; i++)
            xK[i] = (double)matrix_x0[i];
        int k = 0;
        double sum = 0;
        while(k < max_k && epsilon.compareTo(new BigDecimal(norm(xK))) < 0) {
            k++;
            for (int i = 0; i < xK.length; i++) {
                for (int j = 0; j < xK.length; j++) {
                    if(j == i) continue;
                    sum += matrix_A[i][j]*xK[j];
                }
                xK[i] = (1-rel_c)*xK[i]+rel_c/matrix_A[i][i]*(matrix_f[i] - sum);
                sum = 0;
            }
        }
        it_num = k;
        matrix_xq = xK;
        for (int i = 0; i < matrix_xq.length; i++)
            error += Math.abs(matrix_xq[i] - matrix_X[i]);
    }

    public static void experem(){
        for (int i = 0; i < rel_c.length; i++) {
            relax(rel_c[i]);
            System.out.println("Number of iterations: "+it_num+" error: "+error+" coefficient: "+rel_c[i]);
        }
    }

    public static void main(String[] args) {
        fill_matrix(10, 6);
        fill_f();
        grad();
        print();
        System.out.println("\nExperiment: ");
        experem();
    }
}
