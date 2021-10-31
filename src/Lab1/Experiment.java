package Lab1;

public class Experiment extends Gauss{

    public static Double[][] experem(int size_temp){
        int size;
        Double errors[][] = new Double[11][1];
        for (int i = 1, count = 0; i < 111; i += 10, count++) {
            size = size_temp + i;
            Double matrix_A[][] = new Double[size][size];
            Double matrix_X[][] = new Double[size][1];
            Double matrix_f[][];
            Double matrix_Solution[][];
            //заполнение матриц
            fill_matrix(matrix_A);
            fill_X(matrix_X);

            //создание столбца свободных членов
            matrix_f = multiply_matrix(matrix_A, matrix_X);
            //выполнение метода Гаусса
            matrix_Solution = Gauss_solution(matrix_A, matrix_f);
            //Анализ погрешностей для текущего столбца решения
            analyze(matrix_X, matrix_Solution, errors, count);
        }
        return errors;
    }

    public static void main(String[] args) {
        print_matrix(experem(6), 17);
    }
}
