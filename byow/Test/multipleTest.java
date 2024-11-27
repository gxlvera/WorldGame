package byow.Test;

import byow.Core.Engine;
import byow.TileEngine.TETile;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class multipleTest {
    @Test

    public void agTest() {

        Engine e1 = new Engine();

        Engine e2 = new Engine();

        TETile[][] result = e1.interactWithInputString("n2838278388919144292ss");

        e2.interactWithInputString("n2838278388919144292ss:q");

        TETile[][] result2 = e2.interactWithInputString("l");

        assertThat(result2).isEqualTo(result);

    }
}
