package ecs.components;

import java.util.HashMap;
import java.util.Map;

public class KeyboardControlled extends Component {
    public Map<Integer, LanderMovement.Moves> keys;
    public Map<LanderMovement.Moves, Integer> lookup;

    public KeyboardControlled(Map<Integer, LanderMovement.Moves> keys) {
        this.keys = keys;

        lookup = new HashMap<>();
        for (var mapping : keys.entrySet()) {
            lookup.put(mapping.getValue(), mapping.getKey());
        }
    }
}
