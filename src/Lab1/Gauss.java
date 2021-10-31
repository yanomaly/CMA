package Lab1;
import java.util.Random;
public class Gauss {

    private static final Double[][] X = new Double[][]{{1.0},{2.0},{3.0},{4.0},{5.0},{6.0},{7.0},{8.0},{9.0},{10.0}};

    public static void fill_matrix(Double matrix[][]){
        //Заполнение матрицы системы случайными числами
        Random elem = new Random();
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                matrix[i][j] = (Math.pow(-1.0, elem.nextInt()) * elem.nextDouble() * 1000);
    }

    public static void print_matrix(Double matrix[][], int accuracy){
        //Вывод матрицы на консоль
        String accur = new String("%." + accuracy + "f");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j] >= 0) {
                    if (matrix[i][j] / 10 >= 1) {
                        System.out.print(" ");
                        System.out.printf(accur, matrix[i][j]);
                    } else {
                        System.out.print("  ");
                        System.out.printf(accur, matrix[i][j]);
                    }
                }
                    else{
                        if (matrix[i][j] / 10 <= -1) {
                            System.out.printf(accur, matrix[i][j]);
                        }
                        else{
                            System.out.print(" ");
                            System.out.printf(accur, matrix[i][j]);
                        }
                    }
                System.out.print(" ");
                }
            System.out.print('\n');
            }
        }

    public static Double[][] multiply_matrix(Double matrix1[][], Double matrix2[][]){
        //Умножение матрицы matrix1 на матрицу matrix2
        Double temp = (double)0;
        if(matrix1[0].length == matrix2.length){
            Double res_matrix[][] = new Double[matrix1.length][matrix2[0].length];
            for (int i = 0; i < matrix2[0].length; i++) {
                for (int j = 0; j < matrix1.length; j++) {
                    for (int k = 0; k < matrix1[0].length; k++) {
                        temp += matrix1[j][k] * matrix2[k][i];
                    }
                    res_matrix[j][i] = temp;
                    temp = 0.0;
                }
            }
            return res_matrix;
        }
        else {
            System.out.println("Can't multiply this matrix :(");
            return matrix1;
        }
    }

    public static Double[][] Gauss_solution(Double matrix_A[][], Double matrix_f[][]){
        Double solution[][] = new Double[matrix_A.length][1];
        Double max_abs;
        Double temp[];
        int max_abs_j;
        double a_i_i, a_i_i1;

        for (int i = 0; i < matrix_A.length; i++) {
//Поиск максимального элемента
            max_abs = Math.abs(matrix_A[i][i]);
            max_abs_j = i;
            for (int j = i; j < matrix_A.length; j++) {
                if(Math.abs(max_abs) < Math.abs(matrix_A[j][i])) {
                    max_abs = matrix_A[j][i];
                    max_abs_j = j;
                }
            }
//Перестановка строки с максимальным элементом
            if(max_abs_j != i) {
                temp = matrix_A[i];
                matrix_A[i] = matrix_A[max_abs_j];
                matrix_A[max_abs_j] = temp;
                temp = matrix_f[i];
                matrix_f[i] = matrix_f[max_abs_j];
                matrix_f[max_abs_j] = temp;
            }
//Деление строки на ведущий элемент
            a_i_i = matrix_A[i][i];
            for (int j = i; j < matrix_A.length; j++)
                matrix_A[i][j] /= a_i_i;
            matrix_f[i][0] /= a_i_i;
//Зануление столбца
            if(i < matrix_A.length - 1) {
                for (int j = i; j < matrix_A.length - 1; j++) {
                    a_i_i1 = matrix_A[j + 1][i];
                    for (int k = 0; k < matrix_A.length; k++) {
                        matrix_A[j + 1][k] -= matrix_A[i][k] * a_i_i1;
                    }
                    matrix_f[j + 1][0] -= matrix_f[i][0] * a_i_i1;
                }
            }
            System.out.println(i + " ");
        }
        System.out.println("\n");
//Обратный ход
        int count = 0;
        double temp_solution = 0;
        for (int i = matrix_A.length - 1; i > -1 ; i--) {
            temp_solution = matrix_f[i][0];
            for (int j = 0, k = matrix_A.length - 1; j < count && k > 0; j++, k--)
                temp_solution -= matrix_A[i][k] * solution[k][0];
            solution[i][0] = temp_solution;
            count++;
            System.out.println(i + " ");
        }
        return solution;
    }

    public static void analyze(Double matrix_X[][], Double matrix_Solution[][], Double errors[][], int count){
        Double matrix_delta[][] = new Double[matrix_X.length][1];
        double max_delta, max_X, error;
        //Разность между точным решением и получившимся
        for (int i = 0; i < matrix_X.length; i++)
            matrix_delta[i][0] = matrix_X[i][0] - matrix_Solution[i][0];

        //Поиск максимального по модулю элемента столбца, состоящего из разностей
        max_delta = Math.abs(matrix_delta[0][0]);
        for (int i = 0; i < matrix_delta.length; i++) {
            if(Math.abs(max_delta) < Math.abs(matrix_delta[i][0]))
                max_delta = matrix_delta[i][0];
        }

        //Поиск максимального по модулю элемента столбца точного решения
        max_X = Math.abs(matrix_X[0][0]);
        for (int i = 0; i < matrix_X.length; i++) {
            if(Math.abs(max_X) < Math.abs(matrix_X[i][0]))
                max_X = matrix_X[i][0];
        }

        //Вычисление абсолютной погрешности и занесение в массив погрешности
        error = Math.abs(max_delta/max_X);
        errors[count][0] = error * 100;
    }

    public static void copy(Double matrix1[][], Double matrix2[][]) {
        for (int i = 0; i < matrix2.length; i++)
            for (int j = 0; j < matrix2[0].length; j++)
                matrix1[i][j] = matrix2[i][j].doubleValue();
    }

    public static void fill_X(Double matrix_X[][]){
        //Cоздание столбца решения определённого размера
        for(int i = 0; i < matrix_X.length; i++)
            matrix_X[i][0] = (double)i;
    }

    public static void main(String[] args) {
        Double test[][] = new Double[1000][1000];
        Double Solut[][];
        Double error[][] = new Double[1][1];
        Double matrix_X[][] = new Double[1000][1];
        fill_matrix(test);
        System.out.println("A filled");
//        System.out.println("Matrix A:");
//        print_matrix(test, 2);
        fill_X(matrix_X );
        System.out.println("X filled");
        Double test_f[][] = (multiply_matrix(test, matrix_X));
        System.out.println("f created");
//        System.out.println("Matrix f:");
//        print_matrix(test_f, 2);
        Solut = Gauss_solution(test, test_f);
        System.out.println("System solved");
//        System.out.println("Exact solution:");
//        print_matrix(matrix_X, 17);
//        System.out.println("Solution:");
//        print_matrix(Solut, 17);
        analyze(X, Solut, error, 0);
        System.out.println("Error analyzed");
        System.out.println("Error(in percents): ");
        print_matrix(error, 17);
    }
}
