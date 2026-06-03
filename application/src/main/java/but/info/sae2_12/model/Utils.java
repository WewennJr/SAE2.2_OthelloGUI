package but.info.sae2_12.model;

import coordinates.Coordinate;

import java.util.Random;
import java.util.Set;

public class Utils {

    public static Coordinate randomElement(Set<Coordinate> set) {
        int randomIndex = new Random().nextInt(set.size());
        int index = 0;
        for (Coordinate element : set) {
            if (index == randomIndex) {
                return element;
            }
            index++;
        }

        throw new RuntimeException("No random element found");
    }
}
