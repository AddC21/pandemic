package pandemic.Map;

import static pandemic.Map.Cities.*;

import java.util.Arrays;
import java.util.List;

public class GameMap {
    public static void createDefaultMap() {
        // Blue Cities
        SanFrancisco.connections = Arrays.asList(Tokyo, Manila, Chicago, LosAngeles);
        Chicago.connections = Arrays.asList(SanFrancisco, LosAngeles, MexicoCity, Atlanta, Montreal);
        Montreal.connections = Arrays.asList(Chicago, NewYork, Washington);
        NewYork.connections = Arrays.asList(Montreal, Washington, London, Madrid);
        Washington.connections = Arrays.asList(Atlanta, NewYork, Montreal, Miami);
        Atlanta.connections = Arrays.asList(Chicago, Washington, Miami);
        London.connections = Arrays.asList(NewYork, Madrid, Paris, Essen);
        Madrid.connections = Arrays.asList(NewYork, London, Paris, Algiers, SaoPaulo);
        Paris.connections = Arrays.asList(London, Madrid, Milan, Essen, Algiers);
        Essen.connections = Arrays.asList(London, Paris, Milan, StPetersburg);
        Milan.connections = Arrays.asList(Essen, Paris, Istanbul);
        StPetersburg.connections = Arrays.asList(Essen, Istanbul, Moscow);

        // Yellow Cities
        LosAngeles.connections = Arrays.asList(SanFrancisco, Chicago, MexicoCity, Sydney);
        MexicoCity.connections = Arrays.asList(LosAngeles, Chicago, Miami, Bogota, Lima);
        Miami.connections = Arrays.asList(Atlanta, Washington, MexicoCity, Bogota);
        Bogota.connections = Arrays.asList(MexicoCity, Miami, Lima, SaoPaulo, BuenosAires);
        Lima.connections = Arrays.asList(MexicoCity, Bogota, Santiago);
        Santiago.connections = List.of(Lima);
        BuenosAires.connections = Arrays.asList(Bogota, SaoPaulo);
        SaoPaulo.connections = Arrays.asList(Bogota, BuenosAires, Madrid, Lagos);
        Lagos.connections = Arrays.asList(SaoPaulo, Kinshasa, Khartoum);
        Kinshasa.connections = Arrays.asList(Lagos, Khartoum, Johannesburg);
        Johannesburg.connections = Arrays.asList(Kinshasa, Khartoum);
        Khartoum.connections = Arrays.asList(Lagos, Kinshasa, Johannesburg, Cairo);

        // Black Cities
        Algiers.connections = Arrays.asList(Madrid, Paris, Istanbul, Cairo);
        Istanbul.connections = Arrays.asList(Milan, Algiers, StPetersburg, Moscow, Baghdad, Cairo);
        Cairo.connections = Arrays.asList(Algiers, Istanbul, Baghdad, Khartoum, Riyadh);
        Moscow.connections = Arrays.asList(StPetersburg, Istanbul, Tehran);
        Baghdad.connections = Arrays.asList(Istanbul, Cairo, Tehran, Riyadh, Karachi);
        Tehran.connections = Arrays.asList(Moscow, Baghdad, Karachi, Delhi);
        Riyadh.connections = Arrays.asList(Cairo, Baghdad, Karachi);
        Karachi.connections = Arrays.asList(Riyadh, Baghdad, Tehran, Delhi, Mumbai);
        Mumbai.connections = Arrays.asList(Karachi, Delhi, Chennai);
        Delhi.connections = Arrays.asList(Tehran, Karachi, Mumbai, Chennai, Kolkata);
        Chennai.connections = Arrays.asList(Mumbai, Delhi, Kolkata, Bangkok, Jakarta);
        Kolkata.connections = Arrays.asList(Delhi, Chennai, Bangkok, HongKong);

        // Red Cities
        Jakarta.connections = Arrays.asList(Chennai, Bangkok, HoChiMinhCity, Sydney);
        Sydney.connections = Arrays.asList(Jakarta, LosAngeles, Manila);
        Manila.connections = Arrays.asList(SanFrancisco, HongKong, HoChiMinhCity, Sydney);
        HoChiMinhCity.connections = Arrays.asList(Jakarta, Bangkok, Manila, HongKong);
        Bangkok.connections = Arrays.asList(Kolkata, Chennai, Jakarta, HoChiMinhCity, HongKong);
        HongKong.connections = Arrays.asList(Kolkata, Bangkok, HoChiMinhCity, Manila, Shanghai, Taipei);
        Shanghai.connections = Arrays.asList(Beijing, Seoul, Tokyo, Taipei, HongKong);
        Beijing.connections = Arrays.asList(Shanghai, Seoul);
        Seoul.connections = Arrays.asList(Beijing, Shanghai, Tokyo);
        Tokyo.connections = Arrays.asList(Seoul, Shanghai, SanFrancisco, Osaka);
        Osaka.connections = Arrays.asList(Tokyo, Taipei);
        Taipei.connections = Arrays.asList(Osaka, Shanghai, HongKong, Manila);
    }
}
