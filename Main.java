import java.util.Scanner;

public class Main { //nome IGUAL o do ARQUIVO
    public static void main(String[] args) {
        
       try (Scanner leitor = new Scanner(System.in)) {

            System.out.println("=== Calculadora ===");
            System.out.println("Escolha a operação:");
            System.out.println("1 - Somar");
            System.out.println("2 - Subtrair");
            System.out.println("3 - Multiplicar");
            System.out.println("4 - Dividir");
            System.out.print("Opção: ");

            int opcao = leitor.nextInt();

            if (opcao < 1 || opcao > 4) {
                System.out.println("Opção inválida!");
                return;
            }

            System.out.print("Digite o primeiro número: ");
            double a = leitor.nextDouble();

            System.out.print("Digite o segundo número: ");
            double b = leitor.nextDouble();

            double resultado;
            String operacao;

            switch (opcao) {
                case 1:
                    resultado = a + b;
                    operacao = "+";
                    break;
                case 2:
                    resultado = a - b;
                    operacao = "-";
                    break;
                case 3:
                    resultado = a * b;
                    operacao = "x";
                    break;
                case 4:
                    if (b == 0) {
                        System.out.println("Erro: divisão por zero!");
                        return;
                    }
                    resultado = a / b;
                    operacao = "÷";
                    break;
                default:
                    System.out.println("Opção inválida!");
                    return;
            }

            System.out.printf("%nResultado: %.2f %s %.2f = %.2f%n", a, operacao, b, resultado);
        }
    }
}
