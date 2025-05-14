package Pandemic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import Pandemic.Cards.CityCard;
import Pandemic.Cards.PlayerCard;
import Pandemic.Map.Cities;
import Pandemic.Map.City;

public class Player {
    String name;
    City city;
    Map<String, List<CityCard>> cityCards;
    List<String> colours = Arrays.asList("blue", "yellow", "black", "red");

    public Player(String name, City startingCity, List<CityCard> dealtCards)
    {
        this.name = name;
        city = startingCity;
        cityCards = new HashMap<>();
        for (String colour : colours)
        {
            cityCards.put(colour, new ArrayList<>());
        }
        sortCards(dealtCards);
    }

    // TODO: Can probably combine into one method changing the travelToOptions

    public void drive(Scanner scanner)
    {
        System.out.println("\nWhich connected city do you want to travel to:");
        for (int i=0; i<city.connections.size(); i++)
        {
            System.out.println(city.connections.get(i) + " [" + i + "]");
        }
        int chosenCity = scanner.nextInt();
        scanner.nextLine();

        // TODO: Add error handling for not choosing available city
        city = city.connections.get(chosenCity);
    }

    public void directFlight(Scanner scanner, List<PlayerCard> discardPile)
    {
        System.out.println("\nWhich colour of city do you want to take a direct flight to:");
        for (int i=0; i<colours.size(); i++)
        {
            System.out.println(colours.get(i) + " [" + i + "]");
        }
        int chosenColourInt = scanner.nextInt();
        scanner.nextLine();
        String chosenColour = colours.get(chosenColourInt);

        System.out.println("\nWhich city do you want to take a direct flight to:");
        List<CityCard> cityCardsOfChosenColour = cityCards.get(chosenColour);
        for (int i=0; i<cityCardsOfChosenColour.size(); i++)
        {
            System.out.println(cityCardsOfChosenColour.get(i) + " [" + i + "]");
        }

        int chosenCityOfChosenColour = scanner.nextInt();
        scanner.nextLine();

        // TODO: Add error handling for not choosing available city
        CityCard chosenCityCard = cityCardsOfChosenColour.remove(chosenCityOfChosenColour);
        city = chosenCityCard.city;

        discardPile.add(chosenCityCard);
    }

    public void charterFlight(Scanner scanner, List<PlayerCard> discardPile)
    {
        CityCard cardForCurrentCity = cityCards.get(city.colour).stream().filter(card -> card.city == city).findFirst().orElse(null);
        if (cardForCurrentCity == null)
        {
            System.out.println("\nYou don't have the card of the city you're in, so can't take a charter flight");
            return;
        }

        System.out.println("\nEnter the name of the city you want to take a charter flight to:");

        String chosenCityName = scanner.nextLine();

        // TODO: Add error handling for not choosing available city
        City chosenCity = Cities.getAllCities().stream().filter(city -> city.name == chosenCityName).findFirst().orElse(null);

        if (chosenCity == null)
        {
            System.out.println("City not found.");
            return;
        }
        city = chosenCity;

        cityCards.get(cardForCurrentCity.getColour()).remove(cardForCurrentCity);
        discardPile.add(cardForCurrentCity);
    }

    public void treatDisease(Scanner scanner)
    {
        System.out.println("\nWhich colour cube do you want to remove:");
        for (int i=0; i<colours.size(); i++)
        {
            System.out.println(colours.get(i) + ": " + city.cubes.get(i) + " cubes. [" + i + "]");
        }

        int chosenColour = scanner.nextInt();
        scanner.nextLine();

        // TODO: Add error handling for not choosing available colour or choosing a colour with 0
        city.loseCubes(1, chosenColour);
    }

    public void shareKnowledge(Scanner scanner, List<Player> otherPlayersInCity)
    {
        // TODO: Add proper handling for invalid inputs
        if (otherPlayersInCity.size() == 0) // If there is no one else in the city
        {
            System.out.println("\nNo one is in " + city + " with you");
            return;
        }

        List<Player> playersInCity = new ArrayList<>(otherPlayersInCity);
        playersInCity.add(this);

        Player playerWithCityCard = null;

        for (Player player : playersInCity) { // For all the players in this city
            if (player.getAllCityCards().stream().anyMatch(pCard -> pCard.city == city)) // If the player has the card for the city we are in
            {
                if(player == this) // If I have the card
                {
                    System.out.println("\nWho do you want to give a card to?");
                    for (int i = 0; i < otherPlayersInCity.size(); i++)
                    {
                        Player otherPlayer = otherPlayersInCity.get(i);
                        System.out.println("\n" + otherPlayer.name + " [" + i + "]:");
                        for (String colour : colours)
                        {
                            if (otherPlayer.cityCards.get(colour).size() == 0) { continue; }
                            System.out.println(colour + ": " + otherPlayer.cityCards.get(colour).stream().map(cityCard -> cityCard.city.name).collect(Collectors.joining(", ")));
                        }
                    }

                    int chosenPlayerInt = scanner.nextInt();
                    scanner.nextLine();

                    Player chosenPlayer = otherPlayersInCity.get(chosenPlayerInt);

                    giveCurrentCityCard(scanner, chosenPlayer);
                    return;
                }
                playerWithCityCard = player; // Save as playerWithCityCard
                break;
            }
        }

        if (playerWithCityCard == null) {
            System.out.println("\nNo one in " + city + " has the " + city + " card");
            return;
        }

        takeCurrentCityCard(scanner, playerWithCityCard);
        return;
    }

    public int discoverCure(Scanner scanner, List<PlayerCard> discardPile)
    {
        // TODO: should need to be at research station to discover cure
        String[] availableColours = colours.stream().filter(colour -> cityCards.get(colour).size() >= 5).toArray(String[]::new);
        
        // TODO: Handling for if there are no diseases available to cure
        System.out.println("\nWhich colour disease do you want to cure:");
        for (int i=0; i<availableColours.length; i++)
        {
            System.out.println(availableColours[i]);
        }

        String chosenColour = scanner.nextLine();

        // TODO: Let them choose which 5 cards to discard if have more than 5
        List<CityCard> cityCardsOfChosenColour = cityCards.get(chosenColour);
        for (int i = 0; i < cityCardsOfChosenColour.size(); i++)
        {
            CityCard card = cityCardsOfChosenColour.remove(0);
            discardPile.add(card);
        }

        return colours.indexOf(chosenColour);
    }

    public void addCard(Scanner scanner, CityCard cityCard)
    {   
        // TODO: Should be a 7 card hand limit, then force discards (or play event cards)
        System.out.println(name + ", you have gained " + cityCard.city + "!");
        String colour = cityCard.getColour();
        cityCards.get(colour).add(cityCard);
        System.out.println("Program resumes after Enter");
        scanner.nextLine(); // Wait for the user to press Enter
    }

    // TODO: Might be nicer to combine these and use a function outside of player to dictate giving/taking player
    public void giveCurrentCityCard(Scanner scanner, Player receivingPlayer)
    {
        // Get card for current city
        CityCard currentCityCard = null;
        for (String colour : colours) {
            List<CityCard> cardsOfColour = cityCards.get(colour);
            for (int i = 0; i < cardsOfColour.size(); i++) {
                if (cardsOfColour.get(i).city.equals(this.city)) {
                    currentCityCard = cardsOfColour.remove(i);
                    break;
                }
            }
            if (currentCityCard != null) {
                break;
            }
        }

        if (currentCityCard == null) {
            System.out.println("Error: Could not find a card matching the city " + this.city);
        }

        receivingPlayer.addCard(scanner, currentCityCard);
    }

    public void takeCurrentCityCard(Scanner scanner, Player givingPlayer)
    {
        // Get card for current city
        CityCard currentCityCard = null;
        for (String colour : colours) {
            List<CityCard> cardsOfColour = givingPlayer.cityCards.get(colour);
            for (int i = 0; i < cardsOfColour.size(); i++) {
                if (cardsOfColour.get(i).city.equals(this.city)) {
                    currentCityCard = cardsOfColour.remove(i);
                    break;
                }
            }
            if (currentCityCard != null) {
                break;
            }
        }

        if (currentCityCard == null) {
            System.out.println("Error: " + givingPlayer.name + " does not have a card matching the city " + this.city);
        }

        addCard(scanner, currentCityCard);
    }

    public void sortCards(List<CityCard> cardsToSort)
    {
        for (CityCard card : cardsToSort)
        {
            String cardColour = card.getColour();
            cityCards.get(cardColour).add(card);
        }
    }

    public void printPlayerOverview()
    {
        System.out.println("\n" + name + ", you are in " + city +".");
        System.out.println(city + ": " + city.cubes.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        System.out.println("Connected to: " + city.connections.stream().map(connectedCity -> connectedCity.name).collect(Collectors.joining(", ")));
        printMyCards();
    }

    private void printMyCards()
    {
        System.out.println("\nYou have these city cards: ");
        printCards();
    }

    public void printPlayerCards()
    {
        System.out.println(name + " \nhas these city cards: ");
        printCards();
    } 

    public void printCards()
    {
        String cardsString = "";
        for (String colour : colours)
        {
            if (cityCards.get(colour).size() == 0) { continue; }
            String colourString = cityCards.get(colour).stream().map(cityCard -> cityCard.city.name).collect(Collectors.joining(" (" + colour + "), "));
            cardsString += colourString + " (" + colour + "), ";
        }
        System.out.println(cardsString.substring(0, cardsString.length() - 2));
    }

    private List<CityCard> getAllCityCards()
    {
        List<CityCard> allCityCards = new ArrayList<>();
        for (String colour : cityCards.keySet())
        {
            allCityCards.addAll(cityCards.get(colour));
        }
        return allCityCards;
    }
}