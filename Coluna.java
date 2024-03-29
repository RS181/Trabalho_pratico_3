import java.util.*;

class Coluna{   //Representa uma coluna na tabela auxiliar
    String nome;                                          //nome da coluna
    private Map<String,Map<String,String>> mapa_coluna;   // variavel -> (classe -> indice's do's exemplos)
    Map<String,Map<String,Set<Integer>>> m;               //variavel -> (class  -> {indece's do's exemplos}
    
    Coluna(){}

    Coluna (String nome,ArrayList<String> col,ArrayList<String[]> examples,int nr_col){
        this.nome = nome;
        mapa_coluna = new HashMap<>();
        m = new HashMap<>();
        Iniatialize(nr_col, col,examples);
        Iniatialize();
        // Pretty_Print();

    }

    void Pretty_Print(){
        // for (String s : mapa_coluna.keySet() ) System.out.println(s + " | " + mapa_coluna.get(s)); System.out.println("====");
        System.out.println( "--------" + nome + "--------");
        for (String s : m.keySet()){
            System.out.println(s + " | " + m.get(s));
        }
        System.out.println("-------------------");
    }
    
    //Inicializa mapa_coluna (ver "definicao" de mapa_coluna acima)
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
    }
    
    
    Set<Integer> helper(String indices){
        Set<Integer> ans = new HashSet<>();
        String[] aux = indices.split(" ");

        for (String s : aux){
            int indice = Integer.valueOf(s);
            ans.add(indice);
        }
        return ans;
    } 

    //inicializa m (ver "definicao" de m acima)
    void Iniatialize(){
        for (String var : mapa_coluna.keySet()){
            Map<String,String> aux = mapa_coluna.get(var);
            Map <String,Set<Integer>> a = new HashMap<>();
            for (String cla : aux.keySet()){
                a.put(cla, helper(aux.get(cla)));
                m.put(var,a);
            }
        }
        // System.out.println("----------fim do teste------------");
    }
}