import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // write your code here
        String one = scanner.next();
        String two = scanner.next();
        int seconds = LocalTime.parse(two).toSecondOfDay() - LocalTime.parse(one).toSecondOfDay();
        System.out.println(Math.abs(seconds));
    }
}