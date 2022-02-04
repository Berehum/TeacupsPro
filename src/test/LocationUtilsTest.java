import io.github.berehum.teacupspro.utils.LocationUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocationUtilsTest {

    @Test
    public void testNumberRange() {
        Assert.assertEquals(-90, LocationUtils.numberRange(-100, 100, 110), 1);
        Assert.assertEquals(90, LocationUtils.numberRange(-100, 100, -110), 1);

        Throwable exception = Assert.assertThrows(UnsupportedOperationException.class, () -> LocationUtils.numberRange(10, -10, 0));
        Assert.assertEquals("Max number must be bigger than min number.", exception.getMessage());
    }


}
