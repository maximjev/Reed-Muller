package reed.muller.encoding.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;

@Service
public class Encoder {

    private int m;
    private int[][] matrix;
    private int width;
    private int height;

    private MatrixService matrixService;

    @Autowired
    public Encoder(MatrixService matrixService) {
        this.matrixService = matrixService;
        this.m = 3;
        this.matrix = new int[m + 1][(int) Math.pow(2, m)];
        this.width = this.matrix[0].length;
        this.height = this.matrix.length;

        this.generateMatrix();
    }


    private int[][] generateMatrix() {
        populateRow(0, width, height - 1);
        return matrix;
    }

    private void populateRow(int partitionWidth, int width, int currentRow) {
        for (int i = (width + partitionWidth) / 2; i < width; i++) {
            matrix[currentRow][i] = 1;
        }
        if (currentRow > 1) {
            populateRow((partitionWidth + width) / 2, width, currentRow - 1);
            populateRow(partitionWidth, (partitionWidth + width) / 2, currentRow - 1);
        }
    }

    public int[] encode(int[] message) {

        int[][] res = new int[message.length / 4][matrix.length];
        int j = 0;
        for (int i = 0; i < message.length; i+=4) {
            int[] t = Arrays.copyOfRange(message, i, i+4);
            int[] temp = matrixService.multiplyByVector(matrix, Arrays.copyOfRange(message, i, i+4));
            res[j] = temp;
            j++;
        }
        return res[1];
    }
}
