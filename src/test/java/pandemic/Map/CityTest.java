package pandemic.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

class CityTest {

    City city;
    City connectedCity;
    Scanner mockScanner;

    @BeforeEach
    void setUp() {
        city = new City("Manchester", "red");
        connectedCity = new City("Liverpool", "blue");

        city.connections.add(connectedCity);
        connectedCity.connections.add(city);

        city.cubes = Arrays.asList(0, 0, 0, 0);
        connectedCity.cubes = Arrays.asList(0, 0, 0, 0);

        mockScanner = mock(Scanner.class); // Won't be used if test is isolated
        when(mockScanner.nextLine()).thenReturn(""); // Mock to return without blocking
    }

    @Test
    void testAddCubes_NoOutbreak() {
        int outbreaks = city.addCubes(2, 1, mockScanner); // Add 2 cubes of color 1
        assertEquals(0, outbreaks);
        assertEquals(2, (int) city.cubes.get(1));
    }

    @Test
    void testAddCubes_OneOutbreak() {
        city.cubes.set(1, 3); // Already has 3 cubes

        int outbreaks = city.addCubes(1, 1, mockScanner);

        assertEquals(1, outbreaks);
        assertEquals(3, (int) city.cubes.get(1)); // Should stay at 3
        assertEquals(1, (int) connectedCity.cubes.get(1)); // One cube pushed to connected city
    }

    @Test
    void testAddCubes_ChainedOutbreaks() {
        city.cubes.set(1, 3);
        connectedCity.cubes.set(1, 3); // This will cause chained outbreak

        int outbreaks = city.addCubes(1, 1, mockScanner);

        assertEquals(2, outbreaks); // City and connectedCity both outbreak exactly once
        assertEquals(3, (int) city.cubes.get(1));
        assertEquals(3, (int) connectedCity.cubes.get(1)); // Should stay at 3
    }
}
