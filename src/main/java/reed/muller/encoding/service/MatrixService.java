package reed.muller.encoding.service;

import org.springframework.stereotype.Service;
import reed.muller.encoding.config.EncodingConfiguration;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

@Service
public class MatrixService {

    private int m;

    private int[][] baseHadamardMatrix = new int[][]{
            new int[]{1, 1},
            new int[]{1, -1}
    };


    public MatrixService(EncodingConfiguration configuration) {
        this.m = configuration.getM();
    }

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
                .mapToLong(col -> IntStream.range(0, matrix.length)
                        .mapToLong(row -> vector[row] * matrix[row][col])
                        .sum()
                ).mapToInt(Math::toIntExact)
                .toArray();
    }

    public int[] vectorMatrixDotProduct(int[] vector, int[][] matrix) {
        int[] result = new int[vector.length];

        for (int i = 0; i < matrix[0].length; i++)
            for (int j = 0; j < vector.length; j++)
                result[i] += vector[j] * matrix[j][i];

        return result;
    }

    public int[][] kroneckerProduct(int[][] matrixA, int[][] matrixB) {
        int[][] result = new int[matrixA.length * matrixB.length][matrixA[0].length * matrixB[0].length];

        for (int i = 0; i < result.length; i += matrixB.length) {
            for (int j = 0; j < result[0].length; j += matrixB[0].length) {
                for (int k = 0; k < matrixB.length; k++) {
                    for (int l = 0; l < matrixB[0].length; l++) {
                        result[i + k][j + l] = matrixA[i / matrixB.length][j / matrixB[0].length] * matrixB[k][l];
                    }
                }
            }
        }

        return result;
    }

    public int[][] identityMatrix(int size) {
        int[][] identityMatrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            identityMatrix[i][i] = 1;
        }
        return identityMatrix;
    }

    public int[][] hadamardMatrix(int i) {
        return kroneckerProduct(
                kroneckerProduct(identityMatrix((int) pow(2, m - i)), baseHadamardMatrix),
                identityMatrix((int) pow(2, i - 1))
        );
    }
}
