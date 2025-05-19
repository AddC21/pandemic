package pandemic.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MapUtilsTest {

    @BeforeAll
    public static void setup() {
        GameMap.createDefaultMap(); // Make sure map is initialized
    }

    @Test
    public void testShortestPathBetweenConnectedCities() {
        City start = Cities.Atlanta;
        City end = Cities.Tokyo;

        List<City> path = MapUtils.findShortestPath(start, end);

        // Basic checks
        assertNotNull(path, "Path should not be null");
        assertEquals(start, path.get(0), "Path should start with the starting city");
        assertEquals(end, path.get(path.size() - 1), "Path should end with the destination city");

        // Path should be one of the valid shortest routes (4 steps expected)
        assertTrue(path.size() <= 5, "Path should be 5 cities long (4 steps)");
    }

    @Test
    public void testPathToSameCity() {
        City city = Cities.Miami;
        List<City> path = MapUtils.findShortestPath(city, city);

        assertNotNull(path);
        assertEquals(1, path.size(), "Path to the same city should contain exactly one city");
        assertEquals(city, path.get(0));
    }
}
