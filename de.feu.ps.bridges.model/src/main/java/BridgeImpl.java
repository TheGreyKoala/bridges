import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim Gremplewski
 */
public class BridgeImpl implements Bridge {

    private final Set<Island> connectedIslands;

    public BridgeImpl(final Island islandA, final Island islandB) {
        if (islandA == null || islandB == null) {
            throw new IllegalArgumentException("None of the connected islands may be null.");
        }

        connectedIslands = new HashSet<>(2);
        connectedIslands.add(islandA);
        connectedIslands.add(islandB);
    }

    @Override
    public Set<Island> getConnectedIslands() {
        return new HashSet<>(connectedIslands);
    }
}
