package pandemic.Cards;

import pandemic.Map.City;

public class CityCard extends PlayerCard{
    public City city;

    public CityCard(City city){
        this.city = city;
    }

    @Override
    public String toString() {
        return city.toString();
    }

    public String getColour() {
        return city.colour;
    }
}