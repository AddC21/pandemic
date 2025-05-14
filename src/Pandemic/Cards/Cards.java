package Pandemic.Cards;

import java.util.ArrayList;
import java.util.List;

import Pandemic.Map.Cities;
import Pandemic.Map.City;

public class Cards {
    public static List<CityCard> createCityCards() {
        List<CityCard> cityCards = new ArrayList<CityCard>();
        for (City city : Cities.getAllCities())
        {
            CityCard cityCard = new CityCard(city);
            cityCards.add(cityCard);
        }
        return cityCards;
    }

    public static List<InfectionCard> createInfectionCards() {
        List<InfectionCard> infectionCards = new ArrayList<InfectionCard>();
        for (City city : Cities.getAllCities())
        {
            InfectionCard infectionCard = new InfectionCard(city);
            infectionCards.add(infectionCard);
        }
        return infectionCards;
    } 
}
