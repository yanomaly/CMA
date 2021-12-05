package Lab4;

import java.util.Arrays;
import java.util.Random;

public class eigenvalue_iteration {

    private static Double epsilon = Math.pow(10, -7);
    private static Double[][] A;
    private static Double[][] vectors;
    private static Double[] eigenvalue;
    private static Double[] nedo_deficiency;
    private static Integer iterations_number = 0;

    ///////////////////////Task 2////////////////////////////

    public static void fill_matrix(int size){
        Random rand = new Random();
        A = new Double[size][];
        vectors = new Double[size][size];
        for (int i = 0; i < size; i++) {
            A[i] = new Double[i + 1];
            for (int j = 0; j < A[i].length; j++) {
              A[i][j] = (double)rand.nextInt(-100, 100);
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i == j)
                    vectors[i][j] = 1.0;
                else
                    vectors[i][j] = 0.0;
            }
        }
    }

    public static Double exit(){
        Double sum = 0.0;
        for (Double[] temp: A) {
            for (int i = 0; i < temp.length - 1; i++)
                sum += temp[i]*temp[i];
        }
        return 2*sum;
    }

    public static void spin(){
        Double[][] A_copy = A;
        Double cos, sin, mu, max_elem;
        Integer i = 0, j = 0;
        while(exit().compareTo(epsilon) >= 0){
            iterations_number++;
            max_elem = Math.abs(A[0][A[0].length - 1]);

            for (int k = 1; k < A.length; k++) {
                for (int l = 0; l < A[k].length - 1; l++) {
                    if(max_elem < Math.abs(A[k][l])) {
                        max_elem = Math.abs(A[k][l]);
                        i = k;
                        j = l;
                    }
                }
            }

            if(A[i][i].compareTo(A[j][j]) == 0){
                cos = 1/Math.sqrt(2);
                sin = cos;
            }
            else{
                mu = 2*A[i][j]/(A[i][i] - A[j][j]);
                cos = Math.sqrt((1 + 1/(Math.sqrt(1 + mu*mu)))/2);
                sin = Math.signum(mu)*Math.sqrt((1 - 1/(Math.sqrt(1 + mu*mu)))/2);
            }

            for (int k = A[i].length - 1; k < A.length; k++) {
                if(A_copy[k].length < j && A_copy[k].length >= i)
                A_copy[k][i] = A_copy[k][i] * cos + A_copy[j][k] * sin;
                if(A_copy[k].length < i && A_copy[k].length >= j)
                A_copy[k][i] = A_copy[i][k] * cos + A_copy[k][j] * sin;
                if(A_copy[k].length < i && A_copy[k].length < j)
                A_copy[k][i] = A_copy[i][k] * cos + A_copy[j][k] * sin;
                if(A_copy[k].length >= i && A_copy[k].length >= j)
                A_copy[k][i] = A_copy[k][i] * cos + A_copy[k][j] * sin;
            }
            for (int k = A[j].length - 1; k < A.length; k++) {
                if(A_copy[k].length < j)
                    A_copy[k][j] = A_copy[k][i] * (-sin) + A_copy[j][k] * cos;
                if(A_copy[k].length < i)
                    A_copy[k][j] = A_copy[i][k] * (-sin) + A_copy[k][j] * cos;
                if(A_copy[k].length < i && A_copy[k].length < j)
                    A_copy[k][j] = A_copy[i][k] * (-sin) + A_copy[j][k] * cos;
                if(A_copy[k].length >= i && A_copy[k].length >= j)
                    A_copy[k][j] = A_copy[k][i] * (-sin) + A_copy[k][j] * cos;
            }
        }
        A = A_copy;
    }

    /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        fill_matrix(3);
        for (Double[] temp:
                A) {
            System.out.println(Arrays.toString(temp));
        }
        spin();
        for (Double[] temp:
             A) {
            System.out.println(Arrays.toString(temp));
        }
    }
}
