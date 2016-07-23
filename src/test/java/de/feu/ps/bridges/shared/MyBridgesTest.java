package de.feu.ps.bridges.shared;

/**
 * @author Tim Gremplewski
 */
public class MyBridgesTest extends  BridgesTest {

    @Override
    public BridgesTester getBridgesTesterImpl() {
        return new BridgesTesterImpl();
    }

    @Override
    public String getMatrNr() {
        return "9514244";
    }

    @Override
    public String getName() {
        return "Tim Gremplewski";
    }

    @Override
    public String getEmail() {
        return "tim.gremplewski@gmail.com";
    }
}
