import java.util.*;
class ROOTNode {
    String name_col;                                        //nome da coluna do no atual
    Map <String ,Set<Integer>> class_indice ;               //Mapa (class -> {indice's} )
    Map<String,Map<String,Set<Integer>>> var_class_ind;     //Mapa (var -> (class -> {indice's}) )  
    double Entropia_R;                                      //Contem a entropia do no atual 
    // ArrayList <ROOTNode> filhos;                        //filhos (caso existam de um no)

    ArrayList<String[]> Example_Population;                 //Populacao de exemplo 
    ArrayList<String> Original_attributes;                  //atributos ou colunas

    Coluna[] colunas;                                       // contem todas as colunas 

    String name_var;                                        //Contem o nome da variavel que este no esta a representar
    Coluna col;                                             //Contem os dados da coluna atual (usamos quando queremos descobrir o best splitting atribute)
    ROOTNode(){

    }

    // Construtor de um no folha 
    ROOTNode(String nome_class){
        name_col = nome_class;
    }

    //Construtor de um no "auxiliar" , para verficar qual e o best spliting node
    
    //todo (CRIAR ESTE CONSTRUTOR PARA OS NOS QUE VAMOS USAR PARA AUXILIAR A ESCOLHA DO 
    //todo do best splitting node)
    ROOTNode(String name_var,Coluna col,ArrayList<String[]> new_examples,ArrayList<String> new_attributes){
        this.name_var = name_var;
        System.out.println(this.name_var);

        //todo criar um iniatilize parecido ao de baixo mas que nao procura
        //todo no array de colunas e depois fazer o calculo de entropia
    }

    // Construtor de um no normal
    ROOTNode(String nome_coluna,Coluna[] colunas,ArrayList<String[]> Example_Population,ArrayList<String> Original_attributes){
        //Inicializamos o no
        this.name_col= nome_coluna;
        this.colunas = colunas; 
        this.Example_Population = Example_Population;
        this.Original_attributes = Original_attributes; 
        
        class_indice = new HashMap<>();
        // filhos = new ArrayList<>();

        Iniatialize();
        // Print_Class_Examples();
        
        Calculate_Entropia();
        // Create_Decisions_Nodes();    //esta no backup

        // Calculate_Information_Gain();  esta no backup 
    }
    @Override
    public String toString(){
        return name_col;
    }

    void Print_Class_Examples(){     //Imprime o mapa com (class -> Indice dos exemplos)
        System.out.printf("------------No:%s------------\n",name_col);
        System.out.println("->"+var_class_ind+"\n");
        for(String c : class_indice.keySet()){
            System.out.println(c + " | " + class_indice.get(c));
        }
    }

    //Inicializam-se os mapa class_indice e var_class_ind
    void Iniatialize(){
        //Procuramos pela coluna que cooresponde a este ROOTNode
        for (int i = 0 ; i < colunas.length -1; i++){
            if (colunas[i].nome.equals(name_col)){
                Map<String,Map<String,Set<Integer>>> aux = colunas[i].m;
                var_class_ind = aux;
                for (String var : aux.keySet()){
                    // System.out.println(var  + " " +aux.get(var));
                    Map<String , Set<Integer>> aux2 = aux.get(var);
                    for (String clas : aux2.keySet()){
                        if (class_indice.get(clas) == null){
                            class_indice.put(clas,aux2.get(clas));
                        }
                        else {
                            Set<Integer> indices = new HashSet<>(class_indice.get(clas));
                            for (int in : aux2.get(clas))
                                indices.add(in);
                            class_indice.put(clas,indices);
                        }
                    }
                }
                break;
            }
        }
    }

    double log2(double v){
        return Math.log(v) / Math.log(2);
    }

    //Todo verificar este calculo
    void Calculate_Entropia(){
        int total = 0;                             //total de exemplos 
        int nr_classes = class_indice.size();      //numero de classes

        for (String s : class_indice.keySet() )
            total += class_indice.get(s).size();
        // System.out.println("Calculo da entropia da coluna "+ name_col);
        // System.out.println("    Total exemplos : " + total);
        // System.out.println("    Numero de classes: " + nr_classes); 

        //Calculo da entropia
        String calc = "";
        for (String s : class_indice.keySet()){
            calc += "(-(" + class_indice.get(s).size() + "/" + total + ")log2(" + class_indice.get(s).size() + "/" + total + ")) * ";
            Entropia_R -= (double)(class_indice.get(s).size()/ (double)total) *log2((double)(class_indice.get(s).size() / (double)total));
            // System.out.println("    " +calc + " = " + Entropia_R) ;
        }
        System.out.println("    Valor de entropia do no atual = " + Entropia_R);
    }
}
