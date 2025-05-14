package Pandemic.Map;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class City {
    public String name;
    public String colour;
    public List<City> connections;
    public List<Integer> cubes;
    public boolean midOutbreak;
    // Bool hasResearchStation;

    public City(String name, String colour) {
        this.name = name;
        this.colour = colour;
        cubes = Arrays.asList(0, 0, 0, 0);;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getTotalCubes() 
    {
        int sum = 0;
        for (int colourCubes : cubes)
        {
            sum += colourCubes;
        }
        return sum;
    }

    public int addCubes(int numOfCubes, int colour, Scanner scanner)
    {
        System.out.println(name + " just got infected! " + numOfCubes + "  cube(s) added");
        if (midOutbreak == true)
        {
            return 0;
        }
        int outbreaks = 0;

        // TODO: might be cleaner without a loop
        for (int i=0; i<numOfCubes; i++)
        {
            int cubesOfColour = cubes.get(colour);
            if (cubesOfColour < 3)
            {
                cubes.set(colour, cubesOfColour + 1);
                continue;
            }

            // if already have 3 cubes of that colour, Outbreak
            System.out.println("There has been an outbreak in " + name + "!");
            scanner.nextLine(); // Wait for the user to press Enter
            outbreaks++;
            for (City city : connections)
            {
                midOutbreak = true;
                outbreaks += city.addCubes(1, colour, scanner);
            }
        }
        // midOutbreak ensures that the outbreaks caused by this city dont cause another here
        midOutbreak = false;

        return outbreaks;
    }

    public void loseCubes(int numOfCubes, int colour)
    {
        // TODO: Add proper handling for if already have 0 of that colour
        for (int i=0; i<numOfCubes; i++)
        {
            int cubesOfColour = cubes.get(colour);
            if (cubesOfColour > 0)
            {
                cubes.set(colour, cubesOfColour - 1);
            }
        }
    }
}
