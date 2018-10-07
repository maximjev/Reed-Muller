package reed.muller.encoding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reed.muller.encoding.service.*;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncodingApplicationTests {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MatrixService matrixService;

    @Autowired
    private Encoder encoder;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private Decoder decoder;

    @Test
    public void contextLoads() {
    }

    @Test
    public void converterToBitsTest() {
        String test = "test";

        int[] expected = new int[]{
                0, 1, 1, 1, 0, 1, 0, 0,
                0, 1, 1, 0, 0, 1, 0, 1,
                0, 1, 1, 1, 0, 0, 1, 1,
                0, 1, 1, 1, 0, 1, 0, 0
        };
        try {
            int[] result = messageConverter.convertToBits(test);

            assertArrayEquals(expected, result);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void converterBitsToMessage() {
        String expected = "test";

        int[] bits = new int[]{
                0, 1, 1, 1, 0, 1, 0, 0,
                0, 1, 1, 0, 0, 1, 0, 1,
                0, 1, 1, 1, 0, 0, 1, 1,
                0, 1, 1, 1, 0, 1, 0, 0
        };
        try {
            String result = messageConverter.convertToMessage(bits);
            assertEquals(expected, result);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void multiplyMatrixByVector() {
        int[][] matrix = new int[][]{
                new int[]{2, 1, 0, 2},
                new int[]{1, 1, 2, 0}
        };

        int[] vector = new int[]{2, 2, 2, 1};
        int[] expected = new int[]{8, 8};
        int[] result = matrixService.multiplyByVector(matrix, vector);

        assertArrayEquals(expected, result);
    }

    @Test
    public void multiplyVectorByMatrix() {
        int[][] matrix = new int[][]{
                new int[]{1, 1, 1, 1, 1, 1, 1, 1},
                new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                new int[]{0, 0, 1, 1, 0, 0, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1}
        };

        int[] vector = new int[]{1, 1, 1, 1};
        int[] expected = new int[]{1, 2, 2, 3, 2, 3, 3, 4};

        int[] result = matrixService.multiplyVectorByMatrix(vector, matrix);

        assertArrayEquals(expected, result);
    }

    @Test
    public void matrixVectorDotProductTest() {
        int[][] matrix = new int[][]{
                new int[]{1, 1, 1, 1, 1, 1, 1, 1},
                new int[]{0, 1, 0, 1, 0, 1, 0, 1},
                new int[]{0, 0, 1, 1, 0, 0, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1},
                new int[]{0, 0, 0, 0, 1, 1, 1, 1}
        };

        int[] vector = new int[]{1, 1, 1, 1, 0, 1, 0, 1};
        int[] expected = new int[]{1, 2, 2, 3, 4, 5, 5, 6};

        int[] result = matrixService.vectorMatrixDotProduct(vector, matrix);

        assertArrayEquals(expected, result);
    }


    @Test(expected = IllegalArgumentException.class)
    public void multiplyMatrixByVectorIllegalArgument() {
        int[][] matrix = new int[][]{
                new int[]{2, 1, 0, 2, 1},
                new int[]{1, 1, 2, 0, 1}
        };

        int[] vector = new int[]{2, 2, 2, 1};
        matrixService.multiplyByVector(matrix, vector);
    }

    @Test
    public void kroneckerProductTest() {
        int[][] matrixA = new int[][]{
                new int[]{0, 1},
                new int[]{1, 0}
        };
        int[][] matrixB = new int[][]{
                new int[]{1, 1},
                new int[]{1, 0}
        };
        int[][] expected = new int[][]{
                new int[]{0, 0, 1, 1},
                new int[]{0, 0, 1, 0},
                new int[]{1, 1, 0, 0},
                new int[]{1, 0, 0, 0},
        };

        int[][] result = matrixService.kroneckerProduct(matrixA, matrixB);
        assertArrayEquals(expected, result);
    }

    @Test
    public void identityMatrixTest() {
        int[][] expected = new int[][]{
                new int[]{1, 0, 0},
                new int[]{0, 1, 0},
                new int[]{0, 0, 1}
        };
        int[][] result = matrixService.identityMatrix(3);
        assertArrayEquals(expected, result);
    }

    @Test
    public void hadamardMatrixTest() {
        int[][] expected = new int[][]{
                new int[]{ 1, 1, 0, 0, 0, 0, 0, 0 },
                new int[]{ 1, -1, 0, 0, 0, 0, 0, 0 },
                new int[]{ 0, 0, 1, 1, 0, 0, 0, 0 },
                new int[]{ 0, 0, 1, -1, 0, 0, 0, 0 },
                new int[]{ 0, 0, 0, 0, 1, 1, 0, 0 },
                new int[]{ 0, 0, 0, 0, 1, -1, 0, 0 },
                new int[]{ 0, 0, 0, 0, 0, 0, 1, 1 },
                new int[]{ 0, 0, 0, 0, 0, 0, 1, -1 },
        };

        int[][] result = matrixService.hadamardMatrix(1);
        assertArrayEquals(expected, result);
    }

    @Test
    public void encodeTest() {
        String test = "test";
        int[][] expected = new int[][]{
                new int[]{ 0, 1, 1, 0, 1, 0, 0, 1},
                new int[]{ 0, 1, 0, 1, 0, 1, 0, 1},
                new int[]{ 0, 1, 1, 0, 0, 1, 1, 0},
                new int[]{ 0, 1, 0, 1, 1, 0, 1, 0},
                new int[]{ 0, 1, 1, 0, 1, 0, 0, 1},
                new int[]{ 0, 0, 1, 1, 1, 1, 0, 0},
                new int[]{ 0, 1, 1, 0, 1, 0, 0, 1},
                new int[]{ 0, 1, 0, 1, 0, 1, 0, 1}
        };
        try {
            int[][] result = encoder.truncateMessage(encoder.encode(messageConverter.convertToBits(test)));

            assertArrayEquals(expected, result);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void fullProcessWithoutNoiseTest() {
        String test = "test";

        try {
            int[][] encoded = encoder.truncateMessage(encoder.encode(messageConverter.convertToBits(test)));
            String decoded = messageConverter.convertToMessage(decoder.decode(encoded));

            assertEquals(test, decoded);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}
