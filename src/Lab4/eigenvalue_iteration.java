package Lab4;

import java.util.Arrays;
import java.util.Random;

public class eigenvalue_iteration {

    private Double epsilon = Math.pow(10, -7);
    private Double[][] A;
    private Double[][] vectors;
    private Double[] eigenvalue;
    private Double[][] nedo_deficiency;
    private Integer iterations_number = 0;

    ///////////////////////Task 2////////////////////////////

    public void fill_matrix(int size){
        Random rand = new Random();
        A = new Double[size][size];
        vectors = new Double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
              A[i][j] = (double)rand.nextInt(-100, 100);
              A[j][i] = A[i][j];
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

    public Double exit(Double[][] _A){
        Double sum = 0.0;
        for (int i = 0; i < _A.length; i++) {
            for (int j = 0; j < _A.length; j++) {
                if(i != j)
                    sum += _A[i][j]*_A[i][j];
            }
        }
        return sum;
    }

    public Double[][] multiply(Double[][] _A, Double[][] _vectors, Double[] _eigenvalue){
        nedo_deficiency = new Double[A.length][A.length];
        double sum;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                sum = 0;
                for (int k = 0; k < A.length; k++) {
                    sum += _A[i][k]*_vectors[k][j];
                    nedo_deficiency[i][j] = sum;
                }
                nedo_deficiency[i][j] -= _eigenvalue[j]*_vectors[i][j];
            }
        }
        return nedo_deficiency;
    }

    public void spin() {
        Double[][] A_copy = new Double[A.length][A.length];
        for (int k = 0; k < A.length; k++) {
            for (int l = 0; l < A.length; l++) {
                A_copy[k][l] = A[k][l];
            }
        }
        Double cos, sin, mu, max_elem;
        Integer i = 0, j = 0;
        while (exit(A_copy).compareTo(epsilon) >= 0) {
            iterations_number++;
            max_elem = 0.0;

            for (int k = 0; k < A_copy.length; k++) {
                for (int l = k + 1; l < A_copy.length; l++) {
                    if (max_elem < Math.abs(A_copy[k][l])) {
                        max_elem = Math.abs(A_copy[k][l]);
                        i = k;
                        j = l;
                    }
                }
            }

            if (A_copy[i][i].compareTo(A_copy[j][j]) == 0) {
                cos = 1 / Math.sqrt(2);
                sin = cos;
            } else {
                mu = 2 * A_copy[i][j] / (A_copy[i][i] - A_copy[j][j]);
                cos = Math.sqrt((1 + 1 / (Math.sqrt(1 + mu * mu))) / 2);
                sin = Math.signum(mu) * Math.sqrt((1 - 1 / (Math.sqrt(1 + mu * mu))) / 2);
            }

            Double[][] temp = new Double[A.length][A.length], tempv = new Double[A.length][A.length];
            for (int k = 0; k < A.length; k++) {
                for (int l = 0; l < A.length; l++) {
                    temp[k][l] = A_copy[k][l];
                    tempv[k][l] = vectors[k][l];
                }
            }

            for (int k = 0; k < A.length; k++) {
                A_copy[k][i] = temp[k][i] * cos + temp[k][j] * sin;
                A_copy[k][j] = temp[k][i] * (-sin) + temp[k][j] * cos;
                vectors[k][i] = tempv[k][i] * cos + tempv[k][j] * sin;
                vectors[k][j] = tempv[k][i] * (-sin) + tempv[k][j] * cos;
            }
            for (int k = 0; k < A.length; k++) {
                for (int l = 0; l < A.length; l++) {
                    temp[k][l] = A_copy[k][l];
                }
            }
            for (int k = 0; k < A.length; k++) {
                A_copy[i][k] = temp[i][k] * cos + temp[j][k] * sin;
                A_copy[j][k] = temp[i][k] * (-sin) + temp[j][k] * cos;
            }

        }
        eigenvalue = new Double[A.length];
        for (int k = 0; k < A.length; k++)
            eigenvalue[k] = A_copy[k][k];
    }

    /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        eigenvalue_iteration a = new eigenvalue_iteration();
        a.fill_matrix(10);
        a.spin();
        System.out.println(a.iterations_number);
        for (Double[] temp:
                a.multiply(a.A, a.vectors, a.eigenvalue)) {
            System.out.println(Arrays.toString(temp));
        }
    }
}
