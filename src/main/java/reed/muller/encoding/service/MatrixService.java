package reed.muller.encoding.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.IntStream;

@Service
public class MatrixService {

    public int[] multiplyByVector(int[][] matrix, int[] vector) {
        if (matrix.length != vector.length) {
            throw new IllegalArgumentException("Matrix height and vector length should match");
        }
        return Arrays.stream(matrix)
                .mapToLong(row ->
                        IntStream.range(0, row.length)
                                .mapToLong(col -> row[col] * vector[col])
                                .sum()
                ).mapToInt(Math::toIntExact)
                .toArray();
    }

    public int[] multiplyVectorByMatrix(int[] vector, int[][] matrix) {
        if (matrix.length != vector.length) {
            throw new IllegalArgumentException("Matrix height and vector length should match");
        }

        return IntStream.range(0, matrix[0].length)
                .mapToLong(col -> Arrays.stream(matrix)
                        .mapToLong(row -> vector[col % vector.length] * row[col])
                                .sum()
                ).mapToInt(Math::toIntExact)
                .toArray();
    }
}
