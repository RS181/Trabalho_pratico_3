import java.util.*;
class ROOTNode {
    String name_col;                                        //nome da coluna do no atual
    Map <String ,Set<Integer>> class_indice ;               //Mapa (class -> {indice's} )
    Map<String,Map<String,Set<Integer>>> var_class_ind;     //Mapa (var -> (class -> {indice's}) )  
    double Entropia_R;                                      //Contem a entropia do no atual 
    double Gain_R;                                          //ganho de informacao neste no
    ArrayList <DecisionNode> filhos;                        //filhos (caso existam de um no)

    ArrayList<String[]> Example_Population;                 //Populacao de exemplo 
    ArrayList<String> Original_attributes;                  //atributos ou colunas

    Coluna[] colunas;                                       // contem todas as colunas 


    ROOTNode(String nome_coluna,Coluna[] colunas,ArrayList<String[]> Example_Population,ArrayList<String> Original_attributes){
        //Inicializamos o no
        this.name_col= nome_coluna;
        this.colunas = colunas; 
        this.Example_Population = Example_Population;
        this.Original_attributes = Original_attributes; 
        
        class_indice = new HashMap<>();
        filhos = new ArrayList<>();


        Iniatialize();
        Print_Class_Examples();
        System.out.println("->"+var_class_ind+"\n");
        
        Calculate_Entropia();
        Create_Decisions_Nodes();

        Calculate_Information_Gain();

    }



    void Calculate_Information_Gain(){      //calcula o ganho de informacao
        System.out.println("----Information Gain----");
        Gain_R = Entropia_R;
        double total = 0;                      //total de exemplos do no atual
        
        String calc = Entropia_R + " ";

        for (String s : class_indice.keySet())
            total += class_indice.get(s).size();
        
        for (DecisionNode f : filhos){
            int total_si = 0;
            for (String clas : f.examples_branch.keySet())
                total_si += f.examples_branch.get(clas).size();
            
            calc += "- ((" + total_si + "/" + total + ") * " + f.Entropia_D + ") " ;
            System.out.println("Branch  " + f.branch + " tem " +total_si + " exemplos");
            
            Gain_R -= (double)total_si/total * f.Entropia_D;
        
        }
        System.out.println(calc );
        System.out.println("Ganho de informacao = " + Gain_R);
        System.out.println("----Fim----");
        
    }


    void Create_Decisions_Nodes(){      //Cria os nos de decisao
        System.out.println("\nnumero de nos decisao = " + var_class_ind.size());

        for (String var : var_class_ind.keySet()){
            /*Nota : new HasMap<>(...) , permite criar um novo 
              Hasmap com os mesmos conteudo de var_class_ind.get(var) 
              evitando a "localidade" ,ou seja , podemos alterar valores 
              num e essas alteracoes nao afetam o outro */
            DecisionNode si = new DecisionNode(var, new HashMap<>(var_class_ind.get(var)), this);
            System.out.println(si);
            filhos.add(si);
        }

    }





    void Print_Class_Examples(){     //Imprime o mapa com (class -> Indice dos exemplos)
        System.out.printf("------------No:%s------------\n",name_col);
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
        System.out.println("Calculo da entropia RN da coluna "+ name_col);
        System.out.println("    Total exemplos : " + total);
        System.out.println("    Numero de classes: " + nr_classes); 

        //Calculo da entropia
        String calc = "";
        for (String s : class_indice.keySet()){
            calc += "(-(" + class_indice.get(s).size() + "/" + total + ")log2(" + class_indice.get(s).size() + "/" + total + ")) * ";
            Entropia_R -= (double)(class_indice.get(s).size()/ (double)total) *log2((double)(class_indice.get(s).size() / (double)total));
            System.out.println("    " +calc + " = " + Entropia_R) ;
        }
        System.out.println("    Valor de entropia do no atual = " + Entropia_R);
    }
}
