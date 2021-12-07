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

    ///////////////////////Task 1////////////////////////////

    public Double norm(Double[] vector){
        Double norm = 0.0;
        for (Double temp: vector) {
            norm += temp*temp;
        }
        return Math.sqrt(norm);
    }

    public void deg(){
        Double[][] A_copy = new Double[A.length][A.length];
        Double[] vector = new Double[A.length], exit = new Double[A.length], vector_n = new Double[A.length];
        Double lambda = 0.0;
        //fill A_copy, vector & vector_n(ext)
        for (int k = 0; k < A.length; k++) {
            vector_n[k] = 0.0;
            exit[k] = 1.0;
            for (int l = 0; l < A.length; l++) {
                A_copy[k][l] = A[k][l];
            }
        }
        vector_n[0] = 1.0;
        System.out.println("Y0:\n" + Arrays.toString(vector_n)+"\n");
        while(norm(exit).compareTo(epsilon) >= 0 || iterations_number > 10000){
            iterations_number++;
            Double sum = 0.0;
            //vector_n->vector
            for (int i = 0; i < A.length; i++)
                vector[i] = vector_n[i];
            //A*vector_n
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A.length; j++) {
                    sum += A_copy[i][j]*vector[j];
                }
                vector_n[i] = sum;
                sum = 0.0;
            }
            Double sk1 = 0.0, sk2 = 0.0;
            for (int i = 0; i < A.length; i++) {
                sk1 += vector_n[i]*vector_n[i];
                sk2 += vector[i]*vector_n[i];
            }
            lambda = sk1/sk2;
            //norm
            Double norma = norm(vector_n);
            for (int i = 0; i < A.length; i++)
                vector_n[i] /= norma;
            //A*vector-A*lambda
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A.length; j++) {
                    sum += A_copy[i][j]*vector[j];
                }
                exit[i] = sum - lambda*vector[i];
                sum = 0.0;
            }
        }
        if(iterations_number > 10000)
            System.out.println("Process diverge!");
        else {
            System.out.println("Iterations number:\n" + iterations_number + "\n");
            iterations_number = 0;
            System.out.println("Eigen value (max abs):\n" + lambda + "\n");
            System.out.println("Eigen vector:");
            for (Double temp : vector) {
                System.out.println("[" + temp + "]");
            }
            System.out.println("\nA*vector-lambda*A:");
            for (Double temp : exit) {
                System.out.println("[" + temp + "]");
            }
            System.out.println("\n||A*vector-lambda*A||:\n" + norm(exit) + "\n");
        }
    }

    ///////////////////////Task 2////////////////////////////

    public Double exit(Double[][] _A){
        //sum (Xij)^2 i!=j
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
        //A*vectors - A*eigenvalue
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
        //A->A_copy
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
            //search max elem + indexes
            for (int k = 0; k < A_copy.length; k++) {
                for (int l = k + 1; l < A_copy.length; l++) {
                    if (max_elem < Math.abs(A_copy[k][l])) {
                        max_elem = Math.abs(A_copy[k][l]);
                        i = k;
                        j = l;
                    }
                }
            }
            //sin cos
            if (A_copy[i][i].compareTo(A_copy[j][j]) == 0) {
                cos = 1 / Math.sqrt(2);
                sin = cos;
            } else {
                mu = 2 * A_copy[i][j] / (A_copy[i][i] - A_copy[j][j]);
                cos = Math.sqrt((1 + 1 / (Math.sqrt(1 + mu * mu))) / 2);
                sin = Math.signum(mu) * Math.sqrt((1 - 1 / (Math.sqrt(1 + mu * mu))) / 2);
            }
            //make copies
            Double[][] temp = new Double[A.length][A.length], tempv = new Double[A.length][A.length];
            for (int k = 0; k < A.length; k++) {
                for (int l = 0; l < A.length; l++) {
                    temp[k][l] = A_copy[k][l];
                    tempv[k][l] = vectors[k][l];
                }
            }
            //recalculate columns
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
            //recalculate rows
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
        eigenvalue_iteration run = new eigenvalue_iteration();
        run.fill_matrix(10);
        run.deg();
        run.spin();
        System.out.println("Iterations number:\n"+run.iterations_number+"\n");
        System.out.println("Eigen values:\n" + Arrays.toString(run.eigenvalue)+"\n");
        System.out.println("Eigen vectors:");
        for (Double[] temp : run.vectors) {
            System.out.println(Arrays.toString(temp));
        }
        System.out.println("\nA*vector-lambda*A:");
        for (Double[] temp : run.multiply(run.A, run.vectors, run.eigenvalue)) {
            System.out.println(Arrays.toString(temp));
        }
    }
}
