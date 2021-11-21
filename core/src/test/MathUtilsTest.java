import io.github.berehum.teacups.utils.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testNumberRange() {
        Assert.assertEquals(-90, MathUtils.numberRange(-100, 100, 110), 1);
        Assert.assertEquals(90, MathUtils.numberRange(-100, 100, -110), 1);

        Throwable exception = Assert.assertThrows(UnsupportedOperationException.class, () -> MathUtils.numberRange(10, -10, 0));
        Assert.assertEquals("Max number must be bigger than min number.", exception.getMessage());
    }

}
