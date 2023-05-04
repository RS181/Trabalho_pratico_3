
import java.util.*;
class Tabela {   //Representar a estrutura de dados auxiliar para calcular a entropia

    int numero_colunas; //numero de colunas a ter em conta
    ArrayList<String[]> examples;     //exemplos que vamos ter em conta
    ArrayList<String> attributes;   //atributos(colunas) que vamos ter em conta
    Coluna [] colunas;      //colunas

    Tabela(int numero_colunas,ArrayList<String[]> examples, ArrayList<String> attributes ){
        this.attributes = attributes;
        this.examples = examples;
        this.numero_colunas = numero_colunas;
        colunas = new Coluna[numero_colunas];
        // for (String [] s : examples)
        //     System.out.println(Arrays.toString(s));
        
        Iniatialize();

        //! Testes para o calculo da entropia
        Coluna aux = colunas[4];
        ROOTNode n = new ROOTNode(aux.nome,colunas,examples,attributes);
    }

    

    void Iniatialize(){
        for (int j = 1 ; j < numero_colunas ; j++){
            ArrayList<String> col = new ArrayList<>();
            for (int i = 0 ; i < examples.size() ; i++){
                col.add(examples.get(i)[j]);
            }
            colunas[j-1] =new Coluna(attributes.get(j), col,examples,numero_colunas);
            System.out.println("-------------------");
        }
    }
}
 