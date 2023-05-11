
import java.util.*;
class Tabela {   //Representar a estrutura de dados auxiliar para calcular a entropia

    int numero_colunas;                //numero de colunas a ter em conta
    ArrayList<String[]> examples;      //exemplos que vamos ter em conta
    ArrayList<String> attributes;      //atributos(colunas) que vamos ter em conta
    Coluna [] colunas;                 //colunas

    Tabela(int numero_colunas,ArrayList<String[]> examples, ArrayList<String> attributes ){
        this.attributes = attributes;
        this.examples = examples;
        this.numero_colunas = numero_colunas;
        colunas = new Coluna[numero_colunas];
        // for (String [] s : examples)
        //     System.out.println(Arrays.toString(s));
        
        Iniatialize();

    }

    //verifica se todos os exeplos sao de uma so classe
    boolean Same_class(){
        Set<String> nr_classes = new HashSet<>();
        
        for (String[] e : examples){
            int size = e.length;
            if (size == 2){
                System.out.println("so temos duas colunas : " + attributes.get(0) + attributes.get(1));
                return false;
            }
            else {
                String clas =  e[e.length-1];
                nr_classes.add(clas);
            }    
        }
        // System.out.println(nr_classes);
        if (nr_classes.size() == 1)
            return true;
        else 
            return false;
    }

    String Most_Common_Class(){
        Map<String,Integer> m = new HashMap<>();        //class->frequencia
        int ind = examples.get(0).length-1;
        for (String[] e :  examples){
            if (m.get(e[ind]) == null)
                m.put(e[ind] ,1);
            else 
                m.put(e[ind],m.get(e[ind]) + 1 );
        }
        System.out.println("->" + m);
        String ans = "";
        int biggest = -1;
        for (String key : m.keySet()){
            if (m.get(key) > biggest){
                ans = key;
                biggest = m.get(key);
            }
        }

        return ans;
    }

    void Iniatialize(){
        for (int j = 1 ; j < numero_colunas ; j++){
            ArrayList<String> col = new ArrayList<>();
            for (int i = 0 ; i < examples.size() ; i++){
                col.add(examples.get(i)[j]);
            }
            colunas[j-1] =new Coluna(attributes.get(j), col,examples,numero_colunas);
        }
    }
}
 