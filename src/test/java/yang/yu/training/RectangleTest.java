package yang.yu.training;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * Created by yyang on 16/6/27.
 */
public class RectangleTest {

    Rectangle instance;

    @Before
    public void setUp() throws Exception {
        instance = new Rectangle(10, 5);
    }

    @Test
    public void getArea() throws Exception {
        instance = new Rectangle(10, 5);
        assertThat(instance.getArea()).isEqualTo(50);
    }

    @Test
    public void isSquare() throws Exception {
        assertThat(new Rectangle(10, 5).isSquare()).isFalse();
        assertThat(new Rectangle(10, 10).isSquare()).isTrue();
    }

    @Test
    public void moveHorizontally() throws Exception {
        instance = new Rectangle(10, 5);
        instance.moveHorizontally(20);
        assertThat(instance.getLowerLeftCoordinate()).isEqualTo(Point.of(20, 0));
        assertThat(instance.getLoweRightCoordinate()).isEqualTo(Point.of(30, 0));
        assertThat(instance.getUpperLeftCoordinate()).isEqualTo(Point.of(20, 5));
        assertThat(instance.getUpperRightCoordinate()).isEqualTo(Point.of(30, 5));
    }

    @Test
    public void moveVertically() throws Exception {
        instance = new Rectangle(10, 5);
        instance.moveVertically(-20);
        assertThat(instance.getLowerLeftCoordinate()).isEqualTo(Point.of(0, -20));
        assertThat(instance.getLoweRightCoordinate()).isEqualTo(Point.of(10, -20));
        assertThat(instance.getUpperLeftCoordinate()).isEqualTo(Point.of(0, -15));
        assertThat(instance.getUpperRightCoordinate()).isEqualTo(Point.of(10, -15));
    }

    @Test
    public void moveTo() throws Exception {
        instance = new Rectangle(10, 5);
        instance.moveTo(Point.of(-20, 30));
        assertThat(instance.getLowerLeftCoordinate()).isEqualTo(Point.of(-20, 30));
        assertThat(instance.getLoweRightCoordinate()).isEqualTo(Point.of(-10, 30));
        assertThat(instance.getUpperLeftCoordinate()).isEqualTo(Point.of(-20, 35));
        assertThat(instance.getUpperRightCoordinate()).isEqualTo(Point.of(-10, 35));
    }

}