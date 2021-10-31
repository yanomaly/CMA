package Lab2;

import java.util.Arrays;
import java.util.Random;

public class Tridiagonal_matrix {
    private static int size = 10;
    private static Integer[] a = new Integer[size];
    private static Integer[] b = new Integer[size];
    private static Integer[] c = new Integer[size];
    private static Integer[] f = new Integer[size];
    private static Integer[] y = new Integer[size];
    private static Double[] alpha = new Double[size-1];
    private static Double[] betta = new Double[size];
    private static Double[] solution = new Double[size];
    private static Random value = new Random();

    public static void fill_y(){
        for (int i = 0; i <size; i++)
            y[i] = i + 1;
    }

    public static void fill_a_b(){
        for (int i = 0; i < size - 1; i++) {
            b[i] = value.nextInt(100) * (int)Math.pow(-1, value.nextInt(100));
            if(b[i].intValue() == 0)
                while(b[i].intValue() == 0)
                    b[i] = value.nextInt(100) * (int)Math.pow(-1, value.nextInt(100));
            a[size - 1 - i] = value.nextInt(100) * (int)Math.pow(-1, value.nextInt(100));
            if(a[size - 1 - i].intValue() == 0)
                while(a[size - 1 - i].intValue() == 0)
                    a[size - 1 - i] = value.nextInt(100) * (int)Math.pow(-1, value.nextInt(100));
        }
    }

    public static void fill_c_f(int param){
        for (int i = 1; i < size - 1; i++) {
            c[i] = Math.abs(a[i]) + Math.abs(b[i]) + param + value.nextInt() % param;
            f[i] = -a[i]*y[i-1] + c[i]*y[i] + -b[i]*y[i+1];
        }
        c[0] = Math.abs(b[0]) + param + value.nextInt() % param;
        f[0] = c[0]*y[0] + -b[0]*y[1];
        c[size-1] = Math.abs(a[size-1]) + param + value.nextInt() % param;
        f[size-1] = -a[size-1]*y[size-2] + c[size-1]*y[size-1];
    }

    public static void algorithm() {
        //Прямая прогонка
        alpha[0] = (double)b[0] / c[0];
        betta[0] = (double)f[0] / c[0];
        for (int i = 1; i < size - 1; i++) {
            double znam = c[i] - a[i]*alpha[i-1];
            alpha[i] = b[i] / znam;
            betta[i] = (f[i] + a[i]*betta[i-1])/znam;
        }
        betta[size-1] = (f[size-1] + a[size-1]*betta[size-2]) / (c[size-1] - a[size-1]*alpha[size-2]);

        //Обратная прогонка
        solution[size-1] = betta[size-1];
        for (int i = size-2; i >= 0; i--)
            solution[i] = alpha[i] * y[i+1] + betta[i];
    }

    public static void analyze(Integer matrix_X[], Double matrix_Solution[], Double errors[], int count){
        Double matrix_delta[] = new Double[matrix_X.length];
        double max_delta, max_X, error;
        //Разность между точным решением и получившимся
        for (int i = 0; i < matrix_X.length; i++)
            matrix_delta[i] = (double)matrix_X[i] - matrix_Solution[i];

        //Поиск максимального по модулю элемента столбца, состоящего из разностей
        max_delta = Math.abs(matrix_delta[0]);
        for (int i = 0; i < matrix_delta.length; i++) {
            if(Math.abs(max_delta) < Math.abs(matrix_delta[i]))
                max_delta = matrix_delta[i];
        }

        //Поиск максимального по модулю элемента столбца точного решения
        max_X = Math.abs(matrix_X[0]);
        for (int i = 0; i < matrix_X.length; i++) {
            if(Math.abs(max_X) < Math.abs(matrix_X[i]))
                max_X = matrix_X[i];
        }

        //Вычисление абсолютной погрешности и занесение в массив погрешности
        error = Math.abs(max_delta/max_X);
        errors[count] = error * 100;
    }

    public static void print_matrix(){
       int matrix[][] = new int[size][size];
        for (int i = 0; i < size; i++)
            matrix[i][i] = c[i];
        for (int i = 0; i < size - 1; i++) {
            matrix[i][i + 1] = -b[i];
            matrix[i+1][i] = -a[i+1];
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j] >= 0) {
                    if (matrix[i][j] / 10 >= 10)
                        System.out.print(" " + matrix[i][j]);
                    if (matrix[i][j] / 10 >= 1 && matrix[i][j] / 10 < 10)
                        System.out.print("  " + matrix[i][j]);
                    if (matrix[i][j] / 10 == 0)
                        System.out.print("   " + matrix[i][j]);
                }
                else{
                    if (matrix[i][j] / 10 <= -10)
                        System.out.print(matrix[i][j]);
                    if (matrix[i][j] / 10 <= -1 && matrix[i][j] / 10 > -10)
                        System.out.print(" " + matrix[i][j]);
                    if (matrix[i][j] / 10 == 0)
                        System.out.print("  " + matrix[i][j]);
                }
                System.out.print(" ");
            }
            System.out.print('\n');
        }
    }

    public static void main(String[] args) {
        Double errors[] = new Double[1];
        Tridiagonal_matrix.fill_y();
        Tridiagonal_matrix.fill_a_b();
        Tridiagonal_matrix.fill_c_f(6);
        System.out.println("Matrix A:");
        Tridiagonal_matrix.print_matrix();
        System.out.println("Vector a: ");
        System.out.println(Arrays.toString(a));
        System.out.println("Vector b: ");
        System.out.println(Arrays.toString(b));
        System.out.println("Vector c: ");
        System.out.println(Arrays.toString(c));
        System.out.println("Vector f: ");
        System.out.println(Arrays.toString(f));
        Tridiagonal_matrix.algorithm();
        System.out.println("Current solution: ");
        System.out.println(Arrays.toString(y));
        System.out.println("Program solution: ");
        System.out.println(Arrays.toString(solution));
        Tridiagonal_matrix.analyze(y, solution, errors, 0);
        System.out.println("Error: " + errors[0] + " %");
    }
}
