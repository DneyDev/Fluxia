import java.util.Scanner;

public class Main { //nome IGUAL o do ARQUIVO
    public static void main(String[] args) {
        
        Scanner leitor = new Scanner(System.in);

        String name= "";
        do{
            System.out.println("Informe um nome: ");
            name = leitor.next();
            

        }while(!name.equalsIgnoreCase("sair"));

        if(name.equalsIgnoreCase("sair")){  
                leitor.close(); /*dentro do if porém fora do loop DO
                para não virar uma Exception e findar por manter o
                loop, caso contrário iria anular o loop
                */
            }
    }
}
