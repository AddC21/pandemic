package pandemic;
import static pandemic.Map.Cities.Atlanta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import pandemic.Map.*;
import pandemic.Cards.*;

public class Game {
    List<String> colours = Arrays.asList("blue", "yellow", "black", "red");
    int[] infectionRateCounter = {2,2,2,3,3,4,4};
    int outbreakCounter;
    int numOfPlayers;
    List<Boolean> curedDiseases;
    int infectionRateIndex;
    List<PlayerCard> playerCards;
    List<PlayerCard> discardPile;
    List<Player> players;
    List<InfectionCard> infectionDiscardPile;
    List<InfectionCard> infectionCards;
    boolean gameOver;
    Scanner scanner;

    public Game(int numOfPlayers) {
        // remainingResearchStations = 6
        // TODO: utilise diseaseCubes to end game if one disease runs out of cubes
        // int[] diseaseCubes = {24, 24, 24, 24};
        gameOver = false;
        curedDiseases = new ArrayList<>(Arrays.asList(false, false, false, false));
        infectionRateIndex = 0;
        this.numOfPlayers = numOfPlayers;
        outbreakCounter = 0;
        GameMap.createDefaultMap();

        setup();
    }

    private void setup()
    {
        // Player deck includes one of each city card,  a given number of epidemic cards and action cards (excluded for now)
        // First, deal each player the right amount of city cards
        discardPile = new ArrayList<>();
        List<CityCard> cityCards = Cards.createCityCards();
        Collections.shuffle(cityCards);

        players = new ArrayList<>();
        for (int i=0; i<numOfPlayers; i++)
        {
            // Give 2 cards to each player and place them in Atlanta
            players.add(new Player("Player " + i, Atlanta, new ArrayList<>(cityCards.subList(0, 2))));
            cityCards.subList(0, 2).clear();
        }

        // Then split the deck, add an epidemic card to each section and shuffle before combining
        playerCards = insertEpidemicCards(cityCards, 4); // numberOfEpidemicCards is difficulty (4-6 recommended)

        // Infection deck is one of each city card
        infectionDiscardPile = new ArrayList<>();
        infectionCards = Cards.createInfectionCards();

        // Infect Cities, 3 cities get 3 cubes, 3 get 2 and 3 get 1
        Collections.shuffle(infectionCards);
        List<List<InfectionCard>> selectedInfectionCards = new ArrayList<>();
        for (int i=0; i<3; i++)
        {
            selectedInfectionCards.add(new ArrayList<>(infectionCards.subList(3 * i, 3 * (i+1))));
        }
        infectionCards.subList(0, 9).clear();
        for (List<InfectionCard> selectedInfectionCardsSet : selectedInfectionCards)
        {
            infectionDiscardPile.addAll(selectedInfectionCardsSet);
        }
        infectInitialCities(selectedInfectionCards);
    }
    
    private List<PlayerCard> insertEpidemicCards(List<CityCard> cityCards, int numberOfEpidemicCards) {
        List<List<PlayerCard>> splitCityCards = new ArrayList<>();
        int chunkSize = (int) Math.ceil((double) cityCards.size() / numberOfEpidemicCards);

        // Splits city cards into even sections, last one left shorter if needed
        for (int i=0; i<cityCards.size(); i+=chunkSize) {
            splitCityCards.add(new ArrayList<>(cityCards.subList(i, Math.min(i + chunkSize, cityCards.size()))));
        }

        // Adds an epidemic card to each section, shuffles it and adds it to the new complete deck
        List<PlayerCard> completeDeck = new ArrayList<>();
        for (List<PlayerCard> subdeck : splitCityCards) {
            subdeck.add(new EpidemicCard());
            Collections.shuffle(subdeck);
            completeDeck.addAll(subdeck);
        }

        return completeDeck;
    }
    
    public void play() {
        scanner = new Scanner(System.in);
        while (!gameOver)
        {
            for (Player player : players)
            {
                printBoard(player);
                player.printPlayerOverview();

                // Each player gets 4 actions on their turn
                for (int i=0; i<4; i++)
                {
                    doAction(player);
                }

                // Once the player has done their actions, they draw 2 player cards
                gameOver = drawCards(player, 2);
                if (gameOver) { break; }
                // Then the infection phase happens
                infectCities();
                
                checkIfGameOver();
                if (gameOver) { break; }
            }

        }
        scanner.close();
    }

    private void printBoard(Player activePlayer)
    {
        System.out.println("Cities with cubes:");

        // Sort cities by least to most cubes for ease of player
        List<City> sortedCitiesWithCubes = Cities.getAllCities()
                                                .stream()
                                                .filter(city -> city.getTotalCubes() > 0)
                                                .sorted(Comparator.comparing(City::getTotalCubes))
                                                .collect(Collectors.toList());
        printCities(sortedCitiesWithCubes, activePlayer);

        System.out.println("\nOther Players:");
        for (Player player : players)
        {
            if (player == activePlayer) { continue; }
            String playerLine = player.name +  " (in " + player.city + ") cards: ";
            
            for (String colour : colours)
            {
                if (player.cityCards.get(colour).isEmpty()) { continue; }
                String colourString = player.cityCards.get(colour).stream().map(cityCard -> cityCard.city.name).collect(Collectors.joining(" (" + colour + "), "));
                playerLine += colourString + " (" + colour + "), ";
            }
            System.out.println(playerLine.substring(0, playerLine.length() - 2));
        }
    }

    private void printCities(List<City> cities, Player player)
    {
        class CityPathInfo {
            final City city;
            final String pathString;
            final String flightPathString;

            public CityPathInfo(City city, String pathString, String flightPathString) {
                this.city = city;
                this.pathString = pathString;
                this.flightPathString = flightPathString;
            }
        }

        List<CityPathInfo> cityPathInfos = new ArrayList<>();
        for (City city : cities) {
            List<City> path = MapUtils.findShortestPath(player.city, city);
            String pathString = MapUtils.formatPath(path);

            List<City> flightPath = MapUtils.findShortestPathWithFlights(player.city, city, player.getAllCityCards());
            String flightPathString = MapUtils.formatPath(flightPath);

            cityPathInfos.add(new CityPathInfo(city, pathString, flightPathString));
        }

        int pathLength = cityPathInfos.stream()
                .mapToInt(info -> info.pathString.length())
                .max()
                .orElse(0);

        int flightPathLength = cityPathInfos.stream()
                .mapToInt(info -> info.flightPathString.length())
                .max()
                .orElse(0);

        // Print table header with correct format specifiers
        System.out.printf(
                "%-16s | %-7s %-7s %-7s %-7s | %-" + pathLength + "s | %-" + flightPathLength + "s%n",
                "      City      ", " Blue ", "Yellow", " Black ", "  Red  ", "Quickest Connected Route", "Quickest Route With Cards"
        );

        // Print a separator line
        System.out.println("-".repeat(56 + pathLength + flightPathLength));

        // Print city rows (assuming city, pathString, flightPathString are defined)
        // Loop over indexes and print each pair
        for (CityPathInfo cityPathInfo : cityPathInfos) {
            String pathString = cityPathInfo.pathString;
            String flightPathString = cityPathInfo.flightPathString;
            City city = cityPathInfo.city;

            System.out.printf(
                    "%-16s |    %-1d       %-1d       %-1d       %-1d    | %-" + pathLength + "s | %-" + flightPathLength + "s%n",
                    city.name,
                    city.cubes.get(0),
                    city.cubes.get(1),
                    city.cubes.get(2),
                    city.cubes.get(3),
                    pathString,
                    flightPathString
            );
        }
    }

    private void doAction(Player player)
    {
        // player performs action. If it impacts things outside of that player, the result is dealt with here
        System.out.print("\nWhat action would you like to perform:" +
        "\n Drive/Ferry to a neighbouring city [1]" +
        "\n Direct flight to a city by discarding that city card [2]" +
        "\n Charter flight to a city by discarding the city card matching the city you are in [3]" +
        "\n Remove 1 disease cube in the city you are in [4]" +
        "\n Give or Take the city card of the city you're in to or from another player if both of you are in the city [5]" +
        "\n Discard 5 city cards of the same colour to cure the disease of that colour [6]" +
        "\n");
        int action = scanner.nextInt();
        scanner.nextLine();

        switch (action) {
            case 1:
                player.drive(scanner);
                break;
            
            case 2:
                player.directFlight(scanner, discardPile);
                break;

            case 3:
                player.charterFlight(scanner, discardPile);
                break;
            
            case 4:
                player.treatDisease(scanner);
                break;
            
            case 5:
                player.shareKnowledge(scanner, players.stream().filter(otherPlayer -> otherPlayer.city == player.city && otherPlayer != player).collect(Collectors.toList()));
                break;
            
            case 6:
                int cured = player.discoverCure(scanner, discardPile);
                curedDiseases.set(cured, true);
                break;
        
            default:
                break;
        }
    }

    private boolean drawCards(Player player, int numOfCards)
    {
        System.out.println("Turn over!");
        for (int i=0; i<numOfCards; i++)
        {
            // Draw card
            if (playerCards.isEmpty())
            {
                System.out.println("\nThe player cards have ran out. You have lost. :(");
                scanner.nextLine(); // Wait for the user to press Enter
                gameOver = true;
                return true;
            }
            PlayerCard drawnCard = playerCards.removeFirst();
            
            
            if (drawnCard instanceof EpidemicCard)
            {
                System.out.println("\nEpidemic!");
                // Trigger epidemic (3 steps)
                // 1) Increase infection rate
                infectionRateIndex++;

                // 2) Draw bottom card from infection deck (so fresh city infected) and give 3 cubes of its colour
                InfectionCard freshCityCard = infectionCards.removeLast();
                City freshCity = freshCityCard.city;
                // TODO: If city's disease eradicated (not just cured), fresh city shouldnt get infected
                outbreakCounter += freshCity.addCubes(3, colours.indexOf(freshCity.colour), scanner);
                infectionDiscardPile.add(freshCityCard);

                // 3) Shuffle infection discard pile and add to top of infection deck
                Collections.shuffle(infectionDiscardPile);
                infectionCards.addAll(0, infectionDiscardPile);
            }
            else
            {
                player.addCard(scanner, (CityCard) drawnCard);
            }
        }
        return false;
    }

    private void infectCities()
    {
        List<InfectionCard> chosenInfectionCards = new ArrayList<>(infectionCards.subList(0, infectionRateCounter[infectionRateIndex]));
        infectionCards.subList(0, infectionRateCounter[infectionRateIndex]).clear();
        System.out.println();
        for (InfectionCard infectionCard : chosenInfectionCards)
        {
            int colourIndex = colours.indexOf(infectionCard.city.colour);
            outbreakCounter += infectionCard.city.addCubes(1, colourIndex, scanner);
        }
        infectionDiscardPile.addAll(chosenInfectionCards);
        
        System.out.println("Program resumes after Enter");
        scanner.nextLine(); // Wait for the user to press Enter
    }

    private void infectInitialCities(List<List<InfectionCard>> selectedInfectionCards)
    {
        for (int i=0; i<selectedInfectionCards.size(); i++)
        {
            List<InfectionCard> citiesForCubes = selectedInfectionCards.get(i);
            for (InfectionCard card : citiesForCubes)
            {
                int cityColour = colours.indexOf(card.city.colour);

                outbreakCounter += card.city.addCubes(3-i, cityColour, scanner);
            }
        }
    }

    private void checkIfGameOver()
    {
        if (curedDiseases.stream().allMatch(bool -> bool))
        {
            System.out.println("You have cured all the diseases. You have won! :)");
            scanner.nextLine(); // Wait for the user to press Enter
            gameOver = true;
            return;
        }
        if (outbreakCounter > 7)
        {
            System.out.println("There have been 8 outbreaks. You have lost. :(");
            scanner.nextLine(); // Wait for the user to press Enter
            gameOver = true;
        }
    }
}
