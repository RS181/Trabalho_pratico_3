import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class Main{
    //Guarda os atributos 
    static ArrayList<String> Attributes = new ArrayList<>();
    
    //Guarda os exemplos 
    static ArrayList<String[]> Examples = new ArrayList<>();

    //Tamanho de cada exemplo
    static int Example_size;

    public static void Read_csv(String name) throws Exception{
        File file1 = new File(name);
        // System.out.println(file1.getAbsolutePath());
        Scanner sc = new Scanner(file1);

        //Leitura e armazenamento de Atributos        
        String[] aux = sc.nextLine().split(",");

        for (String attribute : aux)
            Attributes.add(attribute);
        
        System.out.println("Atributos :" + Attributes );

        //Leitura e armazenamento de Exemplos
        Example_size = Attributes.size();

        sc.useDelimiter(",");


        while(sc.hasNextLine()){
            // Examples.add(sc.nextLine());
            String[] temp = sc.nextLine().split(",");

            Examples.add(temp);
      
        }
        sc.close();
        
        int i = 1;
        for (String[] example : Examples){
            System.out.println("Exemplo " + i + ": " + Arrays.toString(example));
            i++;
        } 
    }

    public static void main(String[] args) throws Exception{
        Read_csv(args[0]);
        
    }
}