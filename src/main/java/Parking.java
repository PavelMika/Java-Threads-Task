import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Parking {
    private List<Car> parkedCars = new ArrayList<>();
    private final int maxCapacity = 50;
    private static int occupied = 40;

    public Parking() {
        for (int i = 0; i < occupied; i++) {
            Car car = new Car(i + 200);
            parkedCars.add(car);
            parkedCars.get(i).setIsParking(true);
            parkedCars.get(i).setParking(this);
            car.start();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getOccupied() {
        return occupied;
    }

    public void upOccupied() {
        occupied++;
    }

    public void downOccupied() {
        occupied--;
    }

    public void addCar(Car car) {
        parkedCars.add(car);
    }

    public void removeCar(Car car) {
        parkedCars.remove(car);
    }
}


