import java.util.Scanner;

public class Main { 
    public static void main(String[] args) {
       
        Scanner leitorNome = new Scanner(System.in);

        String userName = leitorNome.nextLine();
        System.out.print("Hello, " + userName);
        leitorNome.close();
    }
}
