import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class Main{
    static ArrayList<String> Attributes = new ArrayList<>();     //Guarda os atributos 
    static ArrayList<String[]> Examples = new ArrayList<>();     //Guarda os exemplos 
    static int Example_size;    //Tamanho de cada exemplo
    static Set<String> Classes = new HashSet<>();     //Classes diferentes que existem no DataSet

    public static void Read_csv(String name) throws Exception{
        File file1 = new File(name);
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
    
    public static void get_Classes(){    //Coloca na variavel o lobal todas as classes diferentes
        for (String[] example : Examples)
            Classes.add(example[example.length-1]) ;
    }

    public static int getPos_col(String attribute){     //retorna o indice que corresponde a um dado atributo
        for (int i = 0 ; i < Examples.size(); i++){
            if (Attributes.get(i).equals(attribute))
                return i;
        }
        System.out.println("Atributo nao existe ou nome de atributo errado");
        return -1;

    }

    public static void main(String[] args) throws Exception{
        Read_csv(args[0]);
        
    
        get_Classes();

        Tabela t = new Tabela(Example_size-1,Examples,Attributes);

        //todo Fazer classe's que representem a Tabela que
        //todo represente valor da variavel(coluna) -> frequencias de valores de class
    }
}