import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car extends Thread {
    private int id;
    private static final int maxWaitingNanos = ThreadLocalRandom.current().nextInt(10, 20);;
    private int maxParkedNanos = ThreadLocalRandom.current().nextInt(10, 100);
    private boolean isParking = false;
    private boolean isLeave = false;
    private final static Lock lock = new ReentrantLock();
    private final static Condition lotAvailable = lock.newCondition();
    private static Parking parking = new Parking();

    public Car(int id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    public static void setParking(Parking parking) {
        Car.parking = parking;
    }

    public void setLeave(boolean leave) {
        isLeave = leave;
    }

    public boolean isParking() {
        return isParking;
    }

    public void setIsParking(boolean parking) {
        isParking = parking;
    }

    private void tryPark() throws InterruptedException {
        try {
            lock.lock();
            if (parking.getOccupied() == parking.getMaxCapacity()) {
                long nanos = lotAvailable.awaitNanos(maxWaitingNanos);
                while (parking.getOccupied() == parking.getMaxCapacity()) {
                    if (nanos <= 0L) {
                        leave();
                        break;
                    }
                }
            }
            if (!isLeave) {
                parking.upOccupied();
                parking.addCar(this);
                setIsParking(true);
                System.out.println("Авто " + getId() + " припарковалось. Свободных мест " + (parking.getMaxCapacity() - parking.getOccupied()));
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            lock.unlock();
        }
    }

    private void leave() {
        try {

            lock.lock();
            if (isParking()) {
                parking.downOccupied();
                parking.removeCar(this);
            }
            goAway();
            setLeave(true);
            lotAvailable.signal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    private void goAway() {
        System.out.println("Авто " + this.getId() + " уезжает. Свободных мест " + (parking.getMaxCapacity() - parking.getOccupied()));
    }


    public void run() {
        try {
            if (!isParking()) {
                tryPark();
            }
            if(isParking)  {
                Thread.sleep(0, maxParkedNanos);
                leave();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}