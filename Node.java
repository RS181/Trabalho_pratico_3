import java.util.ArrayList;
//! estas classes apenas servem para facilitar o percorrer a arvore

class Branch {
    String branch;                  //representa a variavel
    ArrayList <Node> sons;          // respresenta o no associado a essa variavel
    
    Branch(String branch){
        this.branch = branch;
        sons = new ArrayList<>();
    }

}

//No 
class Node{
    String name;                        //nome de um no
    ArrayList<Branch> vars;               //filhos de um no

    Node (String name){
        this.name = name;
        vars = new ArrayList<>();
    }
}


