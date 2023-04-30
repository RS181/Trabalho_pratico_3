import java.util.*;
//todo Confirmar se isto sao os atributos necessarios a um no
class Node {
    //?Atributos de um no
    String name_col;     //nome da coluna do no atual
    String name_of_edge;    //nome da aresta  pai -> filho 
    Node Pai;   //Pai do no atual 
    ArrayList <Node> filhos;     //filhos (caso existam de um no)

    //? Calculo de entropia 
    Map <String ,Set<Integer>> map ;    //Mapa (class , indice's do exemplo)
    double Entropia;    //Contem a entropia do no atual 


    Node(String nome_coluna, Map<String,Map<String,String>> mapa_coluna){
        //Inicializamos as estruturas de dados 
        map = new HashMap<>();
        filhos = new ArrayList<>();

        //Inicializamos o no
        this.name_col= nome_coluna;

        for (String s : mapa_coluna.keySet())
            Iniatialize(mapa_coluna.get(s));
        Print_Class_Examples();
        Calculate_Entropia();
    }

    void Print_Class_Examples(){     //Imprime o mapa com (class -> Indice dos exemplos)
        System.out.printf("------------No:%s------------\n",name_col);
        for(String c : map.keySet()){
            System.out.println(c + " | " + map.get(c));
        }
    }

    void Iniatialize(Map<String,String> class_examples){
        for (String key : class_examples.keySet()){
            String[] aux = class_examples.get(key).split(" ");
            for (String indice : aux){
                if (map.get(key) == null){
                    
                    Set <Integer> s = new HashSet<>();
                    s.add(Integer.valueOf(indice));
                    map.put(key,s);
                }
                else {
                    Set<Integer> s = map.get(key);
                    s.add(Integer.valueOf(indice));
                    map.put(key,s);
                }
            }
        }
    }

    double log2(double v){
        return Math.log(v) / Math.log(2);
    }

    //Todo verificar este calculo
    void Calculate_Entropia(){
        int total = 0;       //total de exemplos 
        int nr_classes = map.size();      //numero de classes

        for (String s : map.keySet() )
            total += map.get(s).size();

        System.out.println("Total exemplos : " + total);
        System.out.println("Numero de classes: " + nr_classes); 
        System.out.println("Entropia = "+ Entropia );

        //Calculo da entropia
        String calc = "";
        for (String s : map.keySet()){
            calc += "(-(" + map.get(s).size() + "/" + total + ")log2(" + map.get(s).size() + "/" + total + ")) * ";
            Entropia -= (double)(map.get(s).size() / (double)total) *log2((double)(map.get(s).size() / (double)total));
        }
        calc += "1";
        System.out.println(calc);
        System.out.println("Valor de entropia do no atual = " + Entropia);
    }
}
