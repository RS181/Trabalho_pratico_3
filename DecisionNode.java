import java.util.*;

//! Se mexer a;guma estrutura de dados que nao pertence a esta
//!classe ter CUIDADO COM LOCALIDADE
class DecisionNode {
    String branch;                                   //variavel que originou este no
    Map<String,Set<Integer>> examples_branch;        //class -> indice do exemplo   
    ROOTNode root;                                   //raiz da arvore de decisao
    DecisionNode pai;                                
    double entropia_D;                               //calculo da entropia

    DecisionNode(String branch ,Map<String,Set<Integer>> examples_branch){
        this.branch = branch;
        this.examples_branch = examples_branch;

    }
}
