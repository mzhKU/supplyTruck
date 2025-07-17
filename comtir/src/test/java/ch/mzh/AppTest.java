package ch.mzh;

import ch.mzh.infrastructure.Position2D;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {

    public AppTest( String testName ) {
        super( testName );
    }

    public static Test suite() {
        return new TestSuite( AppTest.class );
    }

    public void testApp() {
        assertTrue( true );
    }


    public void testInitializeGrid() {
        int width = 3;
        int height = 2;
        Position2D[] gridPositions = new Position2D[width*height];

        int j = 0;
        for(int i = 0; i < width*height; i++) {
            int posX = i % width;
            gridPositions[i] = new Position2D(posX, j);
            if (posX == width - 1) {
                j++;
            }
        }

        assertTrue(true);
    }

    public void testRunningIndex() {
        int width = 3;
        int height = 2;
        for(int i = 0; i<width*height; i++) {
            System.out.println(i%width);
        }
    }
}
