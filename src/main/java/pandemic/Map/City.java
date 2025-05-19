package pandemic.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class City {
    List<String> colours = Arrays.asList("blue", "yellow", "black", "red");
    public String name;
    public String colour;
    public List<City> connections;
    public List<Integer> cubes;
    public boolean midOutbreak;
    // boolean hasResearchStation;

    public City(String name, String colour) {
        this.name = name;
        this.colour = colour;
        cubes = Arrays.asList(0, 0, 0, 0);
        connections = new ArrayList<>();
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
        if (midOutbreak)
        {
            return 0;
        }
        int outbreaks = 0;

        // TODO: might be cleaner without a loop
        int cubesOfColour = cubes.get(colour);

        int cubesCanAdd = numOfCubes;
        if (cubesOfColour + numOfCubes > 3)
        {
            cubesCanAdd = 3 - cubesOfColour;
        }

        cubes.set(colour, cubesOfColour + cubesCanAdd);
        System.out.println(numOfCubes + " " + colours.get(colour) + " cube(s) added to " + name + ". Now there are " + cubes.get(colour) + " " + colours.get(colour) + " cubes in " + name + ".");

        if (cubesCanAdd == numOfCubes) { return 0; }

        // Else, Outbreak!
        System.out.println("Can not add any more " + colours.get(colour) + " cube(s) to " + name);
        System.out.println("There has been an outbreak in " + name + "!");
        scanner.nextLine();
        outbreaks++;
        midOutbreak = true;
        for (City city : connections)
        {
            outbreaks += city.addCubes(1, colour, scanner);
        }
        System.out.println("The outbreak in " + name + " is over.\n");

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
