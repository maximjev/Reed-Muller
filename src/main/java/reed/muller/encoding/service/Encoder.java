package reed.muller.encoding.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reed.muller.encoding.config.EncodingConfiguration;

import java.util.Arrays;

@Service
public class Encoder {

    private int m;
    private int[][] generatorMatrix;

    private MatrixService matrixService;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public Encoder(MatrixService matrixService,
                   EncodingConfiguration configuration) {
        this.matrixService = matrixService;
        this.m = configuration.getM();
        this.generatorMatrix = new int[m + 1][(int) Math.pow(2, m)];
        int width = this.generatorMatrix[0].length;
        int height = this.generatorMatrix.length;

        generatorMatrix(0, width, height);
    }

    private void generatorMatrix(int partitionWidth, int width, int currentRow) {
        for (int i = (width + partitionWidth) / 2; i < width; i++) {
            generatorMatrix[currentRow-1][i] = 1;
        }
        if (currentRow > 1) {
            generatorMatrix((partitionWidth + width) / 2, width, currentRow - 1);
            generatorMatrix(partitionWidth, (partitionWidth + width) / 2, currentRow - 1);
        }
    }

    public int[][] encode(int[] message) {
        LOG.debug("Will encode message");
        int matrixHeight = this.m + 1;

        int[][] result = new int[calculateSize(message.length, matrixHeight)][generatorMatrix[0].length];
        int j = 0;
        for (int i = 0; i < message.length; i += matrixHeight) {
            int[] t = Arrays.copyOfRange(message, i, i + matrixHeight);

            result[j] = matrixService.multiplyVectorByMatrix(t, generatorMatrix);
            j++;
        }
        LOG.debug("Encoding finished");

        return result;
    }

    private int calculateSize(int messageLength, int matrixHeight) {
        if(messageLength % matrixHeight == 0) {
            return messageLength / matrixHeight;
        } else {
            return messageLength / matrixHeight + 1;
        }
    }

    public int[][] truncateMessage(int[][] message) {
        for (int i = 0; i < message.length; i++) {
            message[i] = truncateLine(message[i]);
        }
        return message;
    }

    private int[] truncateLine(int[] line) {
        for (int i = 0; i < line.length; i++) {
            line[i] = line[i] % 2;
        }
        return line;
    }
}
