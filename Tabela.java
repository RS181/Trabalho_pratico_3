// 28/04/2023 13:54

import java.util.*;

class Coluna{   //Representa uma coluna na tabela auxiliar
    String nome;    //nome da coluna
    Map<String,Map<String,String>> mapa_coluna;   // variavel -> (classe -> indice's do's exemplos)
    
    Coluna (String nome,ArrayList<String> col,ArrayList<String[]> examples,int nr_col){
        this.nome = nome;
        mapa_coluna = new HashMap<>();
        System.out.println( "--------" + nome + "--------");
        Iniatialize(nr_col, col,examples);
    }

    void Prety_Print(){
        for (String s : mapa_coluna.keySet() )
            System.out.println(s + " | " + mapa_coluna.get(s));
    }
    
    //todo verificar este metodo
    void Iniatialize(int nr_col,ArrayList<String> col,ArrayList<String[]> examples){
        for (int i = 0 ; i < examples.size() ;i++){
            String Goal = examples.get(i)[nr_col];

            if (mapa_coluna.get(col.get(i)) == null){
                Map<String ,String> aux = new HashMap<>();
                aux.put(Goal, i + "");
                mapa_coluna.put(col.get(i), aux);
            }
            else {
                Map<String ,String> aux = mapa_coluna.get(col.get(i));
                if (aux.get(Goal) == null){
                    aux.put(Goal, i + "");
                }
                else 
                    aux.put(Goal, aux.get(Goal) + " " +i);
            }
        }
        Prety_Print();
    }
}


public class Tabela {   //Representar a estrutura de dados auxiliar para calcular a entropia

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
