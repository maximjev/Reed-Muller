package reed.muller.encoding.service;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reed.muller.encoding.config.EncodingConfiguration;

import static java.lang.Math.*;

@Service
public class Decoder {

    private int m;

    private MatrixService matrixService;

    @Autowired
    public Decoder(MatrixService matrixService,
                   EncodingConfiguration configuration) {
        this.matrixService = matrixService;
        this.m = configuration.getM();
    }

    private int[] replaceZeros(int[] message) {
        for (int i = 0; i < message.length; i++) {
            if (message[i] == 0) {
                message[i] = -1;
            }
        }
        return message;
    }

    public int[] decode(int[][] message) {
        int[] flattedResult = new int[0];

        for (int i = 0; i < message.length; i++) {
            flattedResult = ArrayUtils.addAll(flattedResult, decodeLine(message[i]));
        }
        return flattedResult;
    }

    private int[] decodeLine(int[] baseLine) {
        int largest = 0;
        int largestIndex = 0;

        int[] line = replaceZeros(baseLine);

        for (int i = 1; i <= m; i++) {
            line = matrixService.vectorMatrixDotProduct(line, matrixService.hadamardMatrix(i));

            for (int j = 0; j < line.length; j++) {
                if (abs(line[j]) > abs(largest)) {
                    largest = line[j];
                    largestIndex = j;
                }
            }
        }
        int[] decodedMessage = toBinaryRep(largestIndex);
        if (largest > 0) {
            decodedMessage[0] = 1;
        }

        return decodedMessage;
    }

    private int[] toBinaryRep(int num) {
        int[] result = new int[m + 1];
        int i = 1;
        while (num != 0) {
            result[i] = num % 2;
            num = num / 2;
            i++;
        }
        return result;
    }
}
