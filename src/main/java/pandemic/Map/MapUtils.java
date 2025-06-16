package pandemic.Map;

import java.util.*;
import java.util.stream.Collectors;

import pandemic.Cards.CityCard;

public class MapUtils {

    public static List<City> findShortestPath(City start, City goal) {
        Queue<List<City>> queue = new LinkedList<>();
        Set<City> visited = new HashSet<>();

        queue.add(List.of(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<City> path = queue.poll();
            City current = path.getLast();

            if (current == goal) return path;

            for (City neighbor : current.connections) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<City> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(newPath);
                }
            }
        }

        return null; // No path found
    }

    private record State(List<City> path, Set<City> availableCards) {}

    public static List<City> findShortestPathWithFlights(City start, City goal, List<CityCard> cards) {
        // Initial set of all available cities from cards
        Set<City> initialCards = cards.stream()
                .map(card -> card.city)
                .collect(Collectors.toSet());

        Queue<State> queue = new LinkedList<>();
        Set<City> visited = new HashSet<>();

        // Start state
        queue.add(new State(List.of(start), new HashSet<>(initialCards)));
        visited.add(start);

        // Add direct flights from start to all available cities
        for (City cardCity : initialCards) {
            if (!cardCity.equals(start)) {
                List<City> newPath = Arrays.asList(start, cardCity);
                Set<City> newCards = new HashSet<>(initialCards);
                newCards.remove(cardCity);
                queue.add(new State(newPath, newCards));
                visited.add(cardCity);
            }
        }

        while (!queue.isEmpty()) {
            State state = queue.poll();
            List<City> path = state.path();
            Set<City> availableCards = state.availableCards();
            City current = path.getLast();

            if (current.equals(goal)) return path;

            // Normal move to neighbors
            for (City neighbor : current.connections) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    List<City> newPath = new ArrayList<>(path);
                    newPath.add(neighbor);
                    queue.add(new State(newPath, new HashSet<>(availableCards)));
                }
            }

            // Add charter flights from current city if possible
            if (availableCards.contains(current)) {
                Set<City> newCards = new HashSet<>(availableCards);
                newCards.remove(current);
                List<City> newPath = new ArrayList<>(path);
                newPath.add(goal);
                queue.add(new State(newPath, newCards));
                visited.add(goal);
            }
        }

        return null; // No path found
    }

    public static String formatPath(List<City> path) {
        if (path == null || path.isEmpty()) {
            return "Unreachable";
        }
        return "(" + (path.size()-1) + ") " + path.stream()
                .map(City::toString)
                .reduce((a, b) -> a + " -> " + b)
                .orElse("");
    }
}
