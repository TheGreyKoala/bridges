import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Gremplewski
 */
public class IslandImpl implements Island {

    private final int xPosition;
    private final int yPosition;
    private final int requiredBridges;
    private final List<Bridge> bridges;

    private Island northNeighbour;
    private Island easthNeighbour;
    private Island southNeighbour;
    private Island westNeighbour;

    public IslandImpl(
        final int xPosition,
        final int yPosition,
        final int requiredBridges) {

        // TODO: Parameter validation

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.requiredBridges = requiredBridges;
        bridges = new ArrayList<>(requiredBridges);
    }

    @Override
    public int getXPosition() {
        return xPosition;
    }

    @Override
    public int getYPosition() {
        return yPosition;
    }

    @Override
    public int getRequiredBridges() {
        return requiredBridges;
    }

    @Override
    public int getRemainingBridges() {
        return requiredBridges - bridges.size();
    }

    @Override
    public Island getNorthNeighbour() {
        return northNeighbour;
    }

    @Override
    public Island getEastNeighbour() {
        return easthNeighbour;
    }

    @Override
    public Island getSouthNeighbour() {
        return southNeighbour;
    }

    @Override
    public Island getWestNeighbour() {
        return westNeighbour;
    }

    @Override
    public IslandStatus getStatus() {
        //TODO: Implement Island.getStatus
        return null;
    }

    @Override
    public void addBridge(Island island) {
        //TODO Parameter validation
        final Bridge bridge = new BridgeImpl(this, island);
        addBridge(bridge);
        island.addBridge(bridge);
    }

    @Override
    public void addBridge(Bridge bridge) {
        //TODO Parameter validation
        bridges.add(bridge);
    }
}
