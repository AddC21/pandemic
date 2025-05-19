package pandemic.Cards;

import java.util.ArrayList;
import java.util.List;

import pandemic.Map.Cities;
import pandemic.Map.City;

public class Cards {
    public static List<CityCard> createCityCards() {
        List<CityCard> cityCards = new ArrayList<>();
        for (City city : Cities.getAllCities())
        {
            CityCard cityCard = new CityCard(city);
            cityCards.add(cityCard);
        }
        return cityCards;
    }

    public static List<InfectionCard> createInfectionCards() {
        List<InfectionCard> infectionCards = new ArrayList<>();
        for (City city : Cities.getAllCities())
        {
            InfectionCard infectionCard = new InfectionCard(city);
            infectionCards.add(infectionCard);
        }
        return infectionCards;
    } 
}
