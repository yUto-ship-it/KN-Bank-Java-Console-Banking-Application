import java.util.Scanner;

public class Atm {
    public void authen() {
        Authenticate authen = new Authenticate();
        Scanner s = new Scanner(System.in);

        Contact c = new Contact();
        int choice;
        while (true) {
            System.out.println("╔═════════════════════════════════════════╗");
            System.out.println("║        Welcome From KN Bank             ║");
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║[1] Create A New Bank Account            ║");
            System.out.println("║[2] Login To Your Account                ║");
            System.out.println("║[3] Reset Password                       ║");
            System.out.println("║[4] Exit                                 ║");
            System.out.println("╚═════════════════════════════════════════╝");
            try {
                choice = Integer.parseInt(s.nextLine().trim());
            } catch (NumberFormatException e) {
                // TODO: handle exception
                System.out.println("Invalid Choice!");
                continue;
            }
            switch (choice) {
                case 1:
                    authen.signUp(s);
                    break;
                case 2:
                    authen.login(s);
                    break;
                case 3:
                    c.contact();
                    break;
                case 4:
                    System.exit(0);
                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Atm a = new Atm();
        a.authen();
    }
}
