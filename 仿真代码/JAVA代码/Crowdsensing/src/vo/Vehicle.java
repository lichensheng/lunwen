package vo;
import java.util.List;

public class Vehicle {
    int vehicleNum;
    private List<Position> trajectories;
    private float cost;

    public Vehicle(int vehicleNum, List<Position> trajectories, float cost) {
        this.vehicleNum = vehicleNum;
        this.trajectories = trajectories;
        this.cost = cost;
    }

    public int getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(int vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public List<Position> getTrajectories() {
        return trajectories;
    }

    public void setTrajectories(List<Position> trajectories) {
        this.trajectories = trajectories;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "No=" + vehicleNum;
    }
}
