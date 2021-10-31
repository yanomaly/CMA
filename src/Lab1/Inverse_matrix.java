package Lab1;

public class Inverse_matrix extends Gauss{

    public static Double[][] Identity_matrix(int size){
        //Создание единичной матрицы определённого размера
        Double matrix_I[][] = new Double[size][size];
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j)
                    matrix_I[i][j] = 1.0;
                else
                    matrix_I[i][j] = 0.0;
            }
        }
        return matrix_I;
    }

    public static Double[][] Inverse(Double matrix[][]){
        Double max_abs;
        Double temp[];
        Double Inverse[][] = new Double[matrix.length][matrix.length];
        int max_abs_j;
        double a_i_i, a_i_i1;
        Double[][] matrix_I = Identity_matrix(matrix.length);

        for (int i = 0; i < matrix.length; i++) {
//Поиск макс элемента
            max_abs = Math.abs(matrix[i][i]);
            max_abs_j = i;
            for (int j = i; j < matrix.length; j++) {
                if(Math.abs(max_abs) < Math.abs(matrix[j][i])) {
                    max_abs = matrix[j][i];
                    max_abs_j = j;
                }
            }
//Перестановка строки с максимальным элементом
            if(max_abs_j != i) {
                temp = matrix[i];
                matrix[i] = matrix[max_abs_j];
                matrix[max_abs_j] = temp;
                temp = matrix_I[i];
                matrix_I[i] = matrix_I[max_abs_j];
                matrix_I[max_abs_j] = temp;
            }
//Деление строки на главный элемент
            a_i_i = matrix[i][i];
            for (int j = i; j < matrix.length; j++)
                matrix[i][j] /= a_i_i;
            for (int j = 0; j < matrix.length; j++)
                matrix_I[i][j] /= a_i_i;
//Верхнетреугольная матрица
            if(i < matrix.length - 1) {
                for (int j = i; j < matrix.length - 1; j++) {
                    a_i_i1 = matrix[j + 1][i];
                    for (int k = i; k < matrix.length; k++)
                        matrix[j + 1][k] -= matrix[i][k] * a_i_i1;
                    for (int k = 0; k < matrix.length; k++)
                        matrix_I[j + 1][k] -= matrix_I[i][k] * a_i_i1;
                }
            }
        }
//Обратный ход
        int count = 0;
        double temp_solution = 0;
        for (int i = matrix.length - 1; i > -1 ; i--) {
            for(int n = 0; n < matrix.length; n++) {
                temp_solution = matrix_I[i][n];
                for (int j = 0, k = matrix.length - 1; j < count && k > 0; j++, k--)
                    temp_solution -= matrix[i][k] * Inverse[k][n];
                Inverse[i][n] = temp_solution;
            }
            count++;
        }
        return Inverse;
    }

    public static void abs(Double matrix[][]){
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                matrix[i][j] = Math.abs(matrix[i][j]);
    }

    public static void main(String[] args) {
        Double test[][] = new Double[10][10];
        Double test_c[][] = new Double[10][10];
        Double test_check[][];
        Double[][] test_I;

        fill_matrix(test);
        copy(test_c, test);

        test_I = Inverse(test);

        System.out.println("Matrix A:");
        print_matrix(test_c, 2);
        System.out.println(" ");

        System.out.println("Matrix A^-1:");
        print_matrix(test_I, 17);
        System.out.println(" ");

        System.out.println("Matrix A*A^-1:");
        test_check = multiply_matrix(test_c, test_I);
        abs(test_check);
        print_matrix(test_check, 0);
    }
}
