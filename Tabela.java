
import java.util.*;

class Tabela { // Representar a estrutura de dados auxiliar para calcular a entropia

    int numero_colunas; // numero de colunas a ter em conta
    ArrayList<String[]> examples; // exemplos que vamos ter em conta
    ArrayList<String> attributes; // atributos(colunas) que vamos ter em conta
    Coluna[] colunas; // colunas

    String best_splitting_attribute;        //guarda o nome do melhor splitting attribute

    Tabela(int numero_colunas, ArrayList<String[]> examples, ArrayList<String> attributes) {
        this.attributes = attributes;
        this.examples = examples;
        this.numero_colunas = numero_colunas;
        colunas = new Coluna[numero_colunas];
        // for (String [] s : examples)
        // System.out.println(Arrays.toString(s));
        Iniatialize();
        Find_Best_Splitting_Attribute();
    }

    void Iniatialize() {
        for (int j = 1; j < numero_colunas; j++) {
            ArrayList<String> col = new ArrayList<>();
            for (int i = 0; i < examples.size(); i++) {
                col.add(examples.get(i)[j]);
            }
            colunas[j - 1] = new Coluna(attributes.get(j), col, examples, numero_colunas);

        }
    }

    int getPos_col(String attribute) { // retorna o indice que corresponde a um dado atributo (coluna)
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).equals(attribute))
                return i;
        }
        System.out.println("Atributo nao existe ou nome de atributo errado");
        return -1;
    }
    
    String[] Remove_Col_From_Examples(String[] e,int col){
        String[] ans = new String[e.length-1];
        int pos = 0;
        for (int i = 0 ; i < e.length ; i++){
            if (i != col) {
                ans[pos] = e[i];
                pos++;
            }
        }
        return ans;
    } 

    //Coloca em new examples todos os elementos em examples
    //que  estejam  a false em indices_remove 
    void Filter_Examples (boolean[] indices_remove,ArrayList<String[]> new_examples,ArrayList<String[]> old_examples){
        for (int i = 0 ; i < indices_remove.length ; i++){
            if (indices_remove[i] == false)
                new_examples.add(old_examples.get(i));
        }        
    }

    // TODO VERIFICAR SE METODO DE ESCOLHA ESTA CORRETO
    // Parte que gera todos os atributos e filhos (e escolhe o
    // melhor splitting atribute)
    

    void Find_Best_Splitting_Attribute() {
        //Objetivo e criar os descedente de um no e determinar 
        //o best splitting attribute
        double Best_Entropy = Double.MAX_VALUE;

        // ? System.out.println("=============");
        for (int k = 0 ; k < colunas.length ; k++){
            if (colunas[k] == null) break;
            Coluna aux = colunas[k];
            //No root com atributo aux.nome
            ROOTNode n = new ROOTNode(aux.nome, colunas, examples, attributes);
            // ? System.out.println("nome do atributo atual : " + n.name_col);
            int col_to_remove = getPos_col(n.name_col);

        

            // System.out.println(aux.m);

            //lista que contem os Filhos no rootNode atual 
            ArrayList<ROOTNode> filhos = new ArrayList<>();

            //Ciclo em que em cada iteracao cria um filhos do root node acima
            for (String var : aux.m.keySet() ){
                // System.out.println("No : "+ var);

                ArrayList<String> new_attributes = new ArrayList<>(attributes);           //Novos atributos
                ArrayList<String[]> new_examples = new ArrayList<>();           //Novos exemplos associado a variavel var

                boolean[] indices_remove = new boolean[examples.size()];        //indica se e para remover um exemplo ou nao
                Arrays.fill(indices_remove,true);

                //Ciclo que auxilia a colacar exemplos e que pertencem a este variavel
                for (String clas : aux.m.get(var).keySet() ){
                    Set<Integer> indices =aux.m.get(var).get(clas); 
                    for (int ex : indices)
                        indices_remove[ex] = false;   
                }
                // System.out.println(Arrays.toString(indices_remove));

                //Atualizamos new_examples (passa a conter apenas os exemplos associados a var)
                Filter_Examples(indices_remove,new_examples,examples);

                // ? for (String [] s :new_examples) 
                    // ? System.out.println(Arrays.toString(s));

                filhos.add(new ROOTNode(var,aux,new_examples,new_attributes));

                // ? System.out.println();
            }


            //todo Confirmar se e assim que decidimos o melhor atributo
            //todo eu escolhi o no cujos filhos tem menor soma de entropia
            //! CONFIRMAR
            double Cur_Gain = -1;
            for (ROOTNode sons : filhos)
                Cur_Gain += sons.Entropia_R;
            
            if (Cur_Gain < Best_Entropy){
                Best_Entropy = Cur_Gain;
                best_splitting_attribute = n.name_col;
            }
            // System.out.println("Soma da entropia dos filhos : "+ Cur_Gain);

            // ? System.out.println("=============");

        }
        
    }
}