package Pandemic.Map;

import java.util.Arrays;
import java.util.List;

public class Cities {
    // Blue Cities
    public static final City SanFrancisco = new City("San Francisco", "blue");
    public static final City Chicago = new City("Chicago", "blue");
    public static final City Montreal = new City("Montreal", "blue");
    public static final City NewYork = new City("New York", "blue");
    public static final City Washington = new City("Washington", "blue");
    public static final City Atlanta = new City("Atlanta", "blue");
    public static final City London = new City("London", "blue");
    public static final City Madrid = new City("Madrid", "blue");
    public static final City Paris = new City("Paris", "blue");
    public static final City Essen = new City("Essen", "blue");
    public static final City Milan = new City("Milan", "blue");
    public static final City StPetersburg = new City("St. Petersburg", "blue");

    // Yellow Cities
    public static final City LosAngeles = new City("Los Angeles", "yellow");
    public static final City MexicoCity = new City("Mexico City", "yellow");
    public static final City Miami = new City("Miami", "yellow");
    public static final City Bogota = new City("Bogotá", "yellow");
    public static final City Lima = new City("Lima", "yellow");
    public static final City Santiago = new City("Santiago", "yellow");
    public static final City BuenosAires = new City("Buenos Aires", "yellow");
    public static final City SaoPaulo = new City("São Paulo", "yellow");
    public static final City Lagos = new City("Lagos", "yellow");
    public static final City Kinshasa = new City("Kinshasa", "yellow");
    public static final City Johannesburg = new City("Johannesburg", "yellow");
    public static final City Khartoum = new City("Khartoum", "yellow");

    // Black Cities
    public static final City Algiers = new City("Algiers", "black");
    public static final City Istanbul = new City("Istanbul", "black");
    public static final City Cairo = new City("Cairo", "black");
    public static final City Moscow = new City("Moscow", "black");
    public static final City Baghdad = new City("Baghdad", "black");
    public static final City Tehran = new City("Tehran", "black");
    public static final City Riyadh = new City("Riyadh", "black");
    public static final City Karachi = new City("Karachi", "black");
    public static final City Mumbai = new City("Mumbai", "black");
    public static final City Delhi = new City("Delhi", "black");
    public static final City Chennai = new City("Chennai", "black");
    public static final City Kolkata = new City("Kolkata", "black");

    // Red Cities
    public static final City Jakarta = new City("Jakarta", "red");
    public static final City Sydney = new City("Sydney", "red");
    public static final City Manila = new City("Manila", "red");
    public static final City HoChiMinhCity = new City("Ho Chi Minh City", "red");
    public static final City Bangkok = new City("Bangkok", "red");
    public static final City HongKong = new City("Hong Kong", "red");
    public static final City Shanghai = new City("Shanghai", "red");
    public static final City Beijing = new City("Beijing", "red");
    public static final City Seoul = new City("Seoul", "red");
    public static final City Tokyo = new City("Tokyo", "red");
    public static final City Osaka = new City("Osaka", "red");
    public static final City Taipei = new City("Taipei", "red");

    public static List<City> getAllCities()
    {
        List<City> allCities = Arrays.asList(
            SanFrancisco,
            Chicago,
            Montreal,
            NewYork,
            Washington,
            Atlanta,
            London,
            Madrid,
            Paris,
            Essen,
            Milan,
            StPetersburg,

            LosAngeles,
            MexicoCity,
            Miami,
            Bogota,
            Lima,
            Santiago,
            BuenosAires,
            SaoPaulo,
            Lagos,
            Kinshasa,
            Johannesburg,
            Khartoum,

            Algiers,
            Istanbul,
            Cairo,
            Moscow,
            Baghdad,
            Tehran,
            Riyadh,
            Karachi,
            Mumbai,
            Delhi,
            Chennai,
            Kolkata,

            Jakarta,
            Sydney,
            Manila,
            HoChiMinhCity,
            Bangkok,
            HongKong,
            Shanghai,
            Beijing,
            Seoul,
            Tokyo,
            Osaka,
            Taipei
        );

        return allCities;
    }
}
