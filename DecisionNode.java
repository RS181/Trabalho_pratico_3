import java.util.*;

//! Se mexer a;guma estrutura de dados que nao pertence a esta
//!classe ter CUIDADO COM LOCALIDADE
class DecisionNode {
    String branch;                                   //variavel que originou este no
    Map<String,Set<Integer>> examples_branch;        //class -> indice do exemplo   
    ROOTNode root;                                   //raiz da arvore de decisao
    DecisionNode pai;                                //No decisao pai (aplica-se quando)
    double Entropia_D;                               //calculo da entropia

    DecisionNode(String branch ,Map<String,Set<Integer>> examples_branch,ROOTNode root){
        this.branch = branch;
        this.examples_branch = examples_branch;
        this.root = root;
        Calculate_Entropia();
    }

    @Override
    public String toString() {
        String aux = "-----Decision Node description-----\n";
        aux += "RootNode = "+ root.name_col + "\n";
        aux += "Branch = " + branch + "\n";
        aux += "Entropia = " + Entropia_D + "\n";
        aux += "Mapa class ->indice : \n";
        for (String s : examples_branch.keySet())
            aux += s + " | " + examples_branch.get(s) + "\n";
        aux += "-----End of description-----\n";
        return aux;
    }

    double log2(double v){
        return Math.log(v) / Math.log(2);
    }


    void Calculate_Entropia(){
        int total = 0;                                  //total de exemplos
        int nr_classes = examples_branch.size();        //numero de classes

        for (String clas : examples_branch.keySet())
            total += examples_branch.get(clas).size();
        
        System.out.println("\nCalculo da entropia DN do branch " + branch);
        System.out.println("    Total exemplos : " + total);
        System.out.println("    Numero de classes : " + nr_classes);

        //calculo da entropia
        String calc = "";

        for (String clas : examples_branch.keySet()){
            calc += "(-(" + examples_branch.get(clas).size() + "/" + total + ")log2(" + examples_branch.get(clas).size() + "/" + total +")) * ";
            Entropia_D -= (double)(examples_branch.get(clas).size() / (double)total) * log2((double)(examples_branch.get(clas).size() / (double) total));
            System.out.println("    " +calc + " = " + Entropia_D) ;
        }
        System.out.println("    Valor de entropia do no atual = " + Entropia_D);
    }

}
