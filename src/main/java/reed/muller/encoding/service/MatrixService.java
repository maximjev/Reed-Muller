package reed.muller.encoding.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.IntStream;

@Service
public class MatrixService {

    public long[] multiplyByVector(int[][] matrix, int[] vector) {
        if (matrix.length != vector.length) {
            throw new IllegalArgumentException("Matrix height and vector length should match");
        }
        return Arrays.stream(matrix)
                .mapToLong(row ->
                        IntStream.range(0, row.length)
                                .mapToLong(col -> row[col] * vector[col])
                                .sum()
                ).toArray();
    }

}
