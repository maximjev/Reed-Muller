package reed.muller.encoding;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reed.muller.encoding.service.Encoder;
import reed.muller.encoding.service.MatrixService;
import reed.muller.encoding.service.MessageConverter;

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
        int[][] matrix = new int[][] {
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
    public void test() {
        String test = "test";

        try {
            int[] bits= messageConverter.convertToBits(test);


            int[] result = encoder.encode(bits);

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
}
