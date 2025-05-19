package pandemic.Cards;

import pandemic.Map.City;

public class InfectionCard{
    public City city;

    public InfectionCard(City city){
        this.city = city;
    }

    @Override
    public String toString() {
        return city.toString();
    }
}