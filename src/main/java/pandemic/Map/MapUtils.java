package pandemic.Map;

import java.util.*;

public class MapUtils {

    public static List<City> findShortestPath(City start, City goal) {
        Queue<List<City>> queue = new LinkedList<>();
        Set<City> visited = new HashSet<>();

        queue.add(List.of(start));
        visited.add(start);

        while (!queue.isEmpty()) {
            List<City> path = queue.poll();
            City current = path.get(path.size() - 1);

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

    public static String formatPath(List<City> path) {
        if (path == null || path.isEmpty()) {
            return "Unreachable";
        }
        return "(" + path.size() + ") " + path.stream()
                .map(City::toString)
                .reduce((a, b) -> a + " -> " + b)
                .orElse("");
    }
}
