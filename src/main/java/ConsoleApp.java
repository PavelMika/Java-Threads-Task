import java.util.Scanner;

public class ConsoleApp {
    private static void printMenu() {
        String task = "Пожалуйста, сделайте выбор: " + "\n" +
                "Введите 1, для старта" + "\n" +
                "Введите 0, для выхода из программы";
        System.out.println("\n" + task + "\n");
    }

    private static void exit() {
        System.exit(0);
    }

    void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;
            printMenu();
            choice = scanner.nextInt();
            printTask(choice);

    }

    private static void printTask(int choice) {
        switch (choice) {
            case 1:
                for (int i = 0; i < 20; i++) {
                    Car car = new Car(i);
                    car.start();
                }
                break;
            case 0:
                exit();
            default:
                System.out.println("Неверный ввод");
                break;
        }
    }
}

