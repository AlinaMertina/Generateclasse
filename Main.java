package buildclasse;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        int p=0 ;
        Scanner scanner = new Scanner(System.in);
        while (p==0) {
            System.out.print("Entrez le chemin du template : ");
            String chemin = scanner.nextLine();
            System.out.print("Pour que les annotation de la classe soit ecrit mettew 1 si non mettez 0  : ");
            int annoted = scanner.nextInt();
            BuildClasse buildClasse = new BuildClasse(chemin,annoted,"systemecommercialle","postgres","promotionproduit","5432","mertina","root");
            buildClasse.buildClassefile();
            System.out.print("pour stoper mettez 1 si non mettez 0 : ");
            p = scanner.nextInt();
        }
        scanner.close();
    }
}
