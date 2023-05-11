import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class Main{
    static ArrayList<String> Attributes = new ArrayList<>();            //Guarda os atributos 
    static ArrayList<String[]> Examples = new ArrayList<>();            //Guarda os exemplos 
    static int Example_size;                                            //Tamanho de cada exemplo
    static Set<String> Classes = new HashSet<>();                       //Classes diferentes que existem no DataSet
    static Tabela tabela;                                               //Estrutura de dados auxiliar (ver classe para perceber o que contem)

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
        
        //! NAO ESTAMOS A TER EM CONTA O ID (primeira coluna do csv)!!!!
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

    public static int getPos_col(String attribute){     //retorna o indice que corresponde a um dado atributo  (coluna)
        for (int i = 0 ; i < Examples.size(); i++){
            if (Attributes.get(i).equals(attribute))
                return i;
        }
        System.out.println("Atributo nao existe ou nome de atributo errado");
        return -1;

    }


    public static ROOTNode ID3(ArrayList<String[]> examples,String Target_Attribute,ArrayList<String> attributes ){
        ROOTNode root = new ROOTNode();


        return root; 
    }
    

    public static void main(String[] args) throws Exception{
        Read_csv(args[0]);
        
        get_Classes();
        
        //Inicializamos a estrutura de dados auxiliar com os exemplos a atributos iniciais
        tabela = new Tabela(Example_size-1,Examples,Attributes);
        for (Coluna  c : tabela.colunas){
            if (c != null)
                c.Pretty_Print();
        }
        //Implementar o id3 aqui
        ID3 (new ArrayList<String[]> (Examples),Attributes.get(Attributes.size()-1),new ArrayList<String> (Attributes));
    }
}