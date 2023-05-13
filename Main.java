import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class Main{
    static ArrayList<String> Attributes = new ArrayList<>();            //Guarda os atributos 
    static ArrayList<String[]> Examples = new ArrayList<>();            //Guarda os exemplos 
    static int Example_size;                                            //Tamanho de cada exemplo
    static Set<String> Classes = new HashSet<>();                       //Classes diferentes que existem no DataSet

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

    public static int Nr_classes(ArrayList<String[]> examples , ArrayList<String> attributes){
        Set<String> classes = new HashSet<>();
        int indice = examples.get(0).length-1;
        // System.out.println("Target attribute = " + attributes.get(indice));

        for (String[] e : examples)
            classes.add(e[indice]);
        
        // System.out.println(classes);


        return classes.size();
    } 

    public static String Most_Common(ArrayList<String[]> examples ){
        int indice = examples.get(0).length-1;
        Map <String,Integer> m = new HashMap<>();
        
        for (String[] e : examples){
            if (m.get(e[indice]) == null)   m.put(e[indice],1);
            else    m.put(e[indice],m.get(e[indice]) + 1); 
        }
        
        System.out.println(m);
        int most = 0;
        String ans = "";
        
        for (String k : m.keySet())
            if (m.get(k) > most){
                ans = k;
                most = m.get(k);
            }

        return ans;
    }

    public static ROOTNode ID3(ArrayList<String[]> examples,String Target_Attribute,ArrayList<String> attributes ){
        ROOTNode root = new ROOTNode();

        //examples so tem uma classe
        if (Nr_classes(examples, attributes) == 1)
            return new ROOTNode(examples.get(0)[examples.get(0).length-1]);
        
        //nao temos mais atributos (attributes.size() == 2 ,ID e class)
        if (attributes.size() == 2)
            return new ROOTNode(Most_Common(examples));
        
        //Caso "geral"        
        else {
            Tabela t =  new Tabela (examples.size()-1,examples,attributes);
            /* 
            for (Coluna c : t.colunas){
                if (c != null)  
                    c.Pretty_Print();
            }
            */
            //TODO implementar a parte de  descobrir o melhor atributo 

        }

        return root; 
    }
    

    public static void main(String[] args) throws Exception{
        Read_csv(args[0]);
        
        get_Classes();
        
        
        //Implementar o id3 aqui
        System.out.println("->"+ ID3 (new ArrayList<String[]> (Examples),Attributes.get(Attributes.size()-1),new ArrayList<String> (Attributes)));
    }
}