/**
 * @author Tim Gremplewski
 */
public interface Island {
    int getXPosition();
    int getYPosition();
    int getRequiredBridges();
    int getRemainingBridges();
    Island getNorthNeighbour();
    Island getEastNeighbour();
    Island getSouthNeighbour();
    Island getWestNeighbour();
    IslandStatus getStatus();
    void addBridge(Island island);
    void addBridge(Bridge bridge);
}
