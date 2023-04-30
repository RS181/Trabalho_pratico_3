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

    void Pretty_Print(){
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
        Pretty_Print();
    }
}