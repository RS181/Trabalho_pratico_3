import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class Main{
    static ArrayList<String> Attributes = new ArrayList<>();            //Guarda os atributos 
    static ArrayList<String[]> Examples = new ArrayList<>();            //Guarda os exemplos 
    static int Example_size;                                            //Tamanho de cada exemplo
    static Set<String> Classes = new HashSet<>();                       //Classes diferentes que existem no DataSet
    static Map<String,Set<String>> atributo_variavel;                   //Guarda os pares atributo -> variaveis (iniciais,serve para ter impressao correta)

    public static void Read_csv(String name) throws Exception{
        File file1 = new File(name);
        Scanner sc = new Scanner(file1);

        //Leitura e armazenamento de Atributos        
        String[] aux = sc.nextLine().split(",");

        for (String attribute : aux)
            Attributes.add(attribute);
        
        // System.out.println("Atributos :" + Attributes );

        //Leitura e armazenamento de Exemplos
        Example_size = Attributes.size();

        sc.useDelimiter(",");

        while(sc.hasNextLine()){
            String[] temp = sc.nextLine().split(",");
            Examples.add(temp);
        }
        sc.close();
        
        //! NAO ESTAMOS A TER EM CONTA O ID (primeira coluna do csv)!!!!
        int i = 1;
        // for (String[] example : Examples){
            // System.out.println("Exemplo " + i + ": " + Arrays.toString(example));
            // i++;
        // } 
    }
    
    public static void get_Classes(){    //Coloca na variavel o lobal todas as classes diferentes
        for (String[] example : Examples)
            Classes.add(example[example.length-1]) ;
    }

    public static int getPos_col(String attribute){     //retorna o indice que corresponde a um dado atributo  (coluna)
        for (int i = 0 ; i < Attributes.size(); i++){
            if (Attributes.get(i).equals(attribute))
                return i;
        }
        System.out.println("Atributo nao existe ou nome de atributo errado");
        return -1;
    }

    public static int Nr_classes(ArrayList<String[]> examples , ArrayList<String> attributes){
        Set<String> classes = new HashSet<>();
        int indice = examples.get(0).length-1;
        // System.out.println("Target attribute = " + attributes.get(indice));

        for (String[] e : examples)
            classes.add(e[indice]);
        
        // System.out.println(classes);


        return classes.size();
    } 

    //remove a String em s que esta na posicoa indice
    public static String[] remove(String[] s,int indice){
        String[] n = new String[s.length-1];
        int i = 0;
        int j = 0;
        while (j < n.length){
            if (i == indice){i++;}

            else{
                n[j] = s[i];
                j++;
                i++;
            }
        }
        // System.out.println(Arrays.toString(n));
        return n;

    }


    //Filtra os exemplos que estao associados
    // a escolhe de um no e respetiva variavel
    public static void Filter_Examples(boolean[] indices_remove,int indice_col_remove,ArrayList<String[]> new_examples,ArrayList<String[]> old_examples){
        for (int i = 0 ; i < indices_remove.length ; i++){
            //se o exemplo pertence aos novos exemplos (nao e necessario remover-lo)
            if (indices_remove[i] == false){
                //remove a coluna indice_col 
                String[] aux = remove(old_examples.get(i), indice_col_remove);
                new_examples.add(aux);
            }
        }   
    }

    public static String Most_Common(ArrayList<String[]> examples ){
        int indice = examples.get(0).length-1;
        Map <String,Integer> m = new HashMap<>();
        
        for (String[] e : examples){
            if (m.get(e[indice]) == null)   m.put(e[indice],1);
            else    m.put(e[indice],m.get(e[indice]) + 1); 
        }
        
        // ? System.out.println(m);
        int most = 0;
        String ans = "";
        
        for (String k : m.keySet())
            if (m.get(k) >= most){
                ans = k;
                most = m.get(k);
            }

        return ans;
    }

    public static ROOTNode ID3(ArrayList<String[]> examples,String Target_Attribute,ArrayList<String> attributes,String Ident ){
        ROOTNode root = new ROOTNode();

        //examples so tem uma classe
        if (Nr_classes(examples, attributes) == 1){
            // System.out.println("entrei aqui");
            System.out.println(Ident + "Class :" + examples.get(0)[examples.get(0).length-1] + " " + examples.size());
            return new ROOTNode(examples.get(0)[examples.get(0).length-1]);
        }
        
        //nao temos mais atributos (attributes.size() <= 2 ,ID e class)
        if (attributes.size() <= 2){
            System.out.println(Ident+"Class : " + Most_Common(examples));
            return new ROOTNode(Most_Common(examples));
        }
        
        //Caso "geral"        
        else {
            //* Best splitting attribute */
            Tabela t =  new Tabela (attributes.size()-1,new ArrayList<>(examples), new ArrayList<>(attributes));
            /*
            System.out.println("-----------------");
            for (String[] e : examples)
                System.out.println(Arrays.toString(e));  
            System.out.println("-----------------");
            */

            String A = t.best_splitting_attribute;

            System.out.println(Ident+"Atributo " + A );
            

            root = new ROOTNode(A,t.colunas,examples,attributes);
            // ? System.out.println(root.var_class_ind);
      
            //*Fim  Best splitting attribute */


            Coluna coluna = new Coluna();      //Coluna que contem estruturas de dados com inf deste no
            for (Coluna c : t.colunas)
                if ( c != null){
                    if (root.name_col.equals(c.nome)){
                        coluna = c;
                    }  
                }

            // System.out.println("=>" + coluna.m);
            // System.out.println("=> " + root.var_class_ind);
            
            //* for each possible value vi of A  */

            //! Caso "especial" em que o no atual nao tem algum das variaveis originais
            if (root.var_class_ind.size() != atributo_variavel.get(root.name_col).size()){
                
                // * Percorremos o atributo_variavel (que contem todas as variaveis)
                // * a procurar a variavel que nao esta a ser imprimida 
                Set<String> temp =atributo_variavel.get(root.name_col);
                // System.out.println(root.var_class_ind);
                for (String v : temp){
                    if (!root.var_class_ind.containsKey(v)){
                        System.out.println(Ident + "      "  + v + ":");
                        System.out.println(Ident + "         " + "Class :" + Most_Common(examples) + " " + examples.size()/2);
                    }
                    

                }

            }

            for (String var : root.var_class_ind.keySet()){
                
                // System.out.println(var + " " + root.var_class_ind.get(var));
                // System.out.println("variavel :" + var);
                // ? System.out.println( "   "+root.name_col +" " + var + " " + root.var_class_ind.get(var));
                ArrayList<String[]> new_examples = new ArrayList<>();
                ArrayList<String> new_attributes = new ArrayList<>();

                boolean[] indices_remove = new boolean[examples.size()];        //indica se e para remover um exemplo ou nao
                Arrays.fill(indices_remove,true);

                int col_to_remove = 0;
                int p = 0;
                
                //coloca os novos atributos em new_attributes (exceto o atributo do no pai)
                for (String s : root.Original_attributes){
                    if (! s.equals(root.name_col)){
                        new_attributes.add(s);
                    }
                    else 
                        col_to_remove = p;
                    p++;
                }

                //Ciclo que auxilia a colocar exemplos e que pertencem a este variavel
                for (String clas :coluna.m.get(var).keySet()){
                    Set<Integer> indices = coluna.m.get(var).get(clas); 
                    for (int ex : indices)
                        indices_remove[ex] = false;                   
                }
                // System.out.println(Arrays.toString(indices_remove));

                //Atualizamos new_examples (passa a conter apenas os exemplos associados a var)
                Filter_Examples(indices_remove,col_to_remove,new_examples,examples);
                
                //? for (String [] s :new_examples) 
                    // ?System.out.println(Arrays.toString(s));

                // ?System.out.println(new_attributes);
      
                ROOTNode branch = new ROOTNode(var,coluna,new_examples, new_attributes);
                
                //* Confirmacao se crio branch corretamente*/
                /*
                ? System.out.println("----------------");
                ? System.out.println(branch.name_var);
                ? System.out.println("Entropia = " +branch.Entropia_R);
                ? System.out.println(branch.class_indice);
                ? System.out.println(branch.Original_attributes);

                ? for (String[] ex : branch.Example_Population)
                ?    System.out.println(Arrays.toString(ex));
                ? System.out.println("----------------");
                 */
                
                //Adicionamos o branch ao no root que inicializamos acima
                root.filhos.add(branch);      
               
                if (new_examples.size() == 0){
                    //criamos um leaf node com label = Most common class in examples
                    System.out.println(Ident + "      " + var + ": Class" + Most_Common(examples) + " " +root.var_class_ind.get(var).size() ); 
                    for (ROOTNode f : root.filhos){
                        if (f.name_var.equals(var)) 
                            f.name_col = Most_Common(examples);
                    }  
                }
                else {
                    // "Debaixo" deste branch adicionamos a subtree respetiva
                    System.out.println(Ident +"      " + var + ":");
                    for (ROOTNode f : root.filhos){
                        if (f.name_var.equals(var))
                            f.filhos.add(ID3(new ArrayList<>(new_examples),Target_Attribute,new ArrayList<>(new_attributes),Ident + "         "));
                    }
                }

            }
                //* fim do for each possible value vi of A  */
        }

        return root; 
    }


    public static void Inialtilize_atributes_var(){
        atributo_variavel = new HashMap<>();

        //percorre todos os atributos (exceto o ultimo, que o target attribute)
        for (int i = 1 ; i < Attributes.size()-1 ; i++){
            String Cur_Atr = Attributes.get(i);
            // System.out.print(Cur_Atr + " = ");
            
            atributo_variavel.put(Cur_Atr, new HashSet<>());
            
            //percorre a coluna i de todos os exemplos e adiciona todas as variveis existentes nessa coluna (sem repetidos)
            for (int j = 0 ; j < Examples.size() ; j++){
                String[] Cur_Ex = Examples.get(j);
                Set<String> aux = atributo_variavel.get(Cur_Atr);
                aux.add(Cur_Ex[i]);
                // System.out.print(Cur_Ex[i] + " ");
            }
            // System.out.println(atributo_variavel.get(Cur_Atr));
            // System.out.println();
        }

    }
    

    public static void main(String[] args) throws Exception{
        //Para criar uma arvore de decisao dado o csv
        if (args[0].contains(".csv")){
            Read_csv(args[0]);

            get_Classes();
            
            Inialtilize_atributes_var();
            // for (String at : atributo_variavel.keySet()){
                // System.out.println( at + " = " + atributo_variavel.get(at));
            // }
            //Implementar o id3 aqui
            
            //* Imprimimos informacao necessaria para adicionar um exemplo (nr de atributos e atributos em si)
            System.out.print (Attributes.size() + " ");
            for (String atr : Attributes)
                System.out.print(atr + " ");
            System.out.println();

            ID3 (new ArrayList<String[]> (Examples),Attributes.get(Attributes.size()-1),new ArrayList<String> (Attributes),"");
        }
        //Para adicionar exemplo
        else {
            Scanner stdin = new Scanner(System.in);
            Attributes = new ArrayList<>();

            //Guarda-mos os atributos (para ajudar na categorizacao)
            int nr_attributes = stdin.nextInt();
            for (int i = 0 ; i < nr_attributes -1 ; i++)
                Attributes.add(stdin.next());

            stdin.nextLine();       //Apanha a classe
            System.out.println(Attributes);
            
            Queue<String> Decision_tree = new  LinkedList<>();
            while(stdin.hasNextLine()){
                Decision_tree.add(stdin.nextLine());
            }

            //todo descobrir como fazer a parte de receber um exemplo e categoriza-lo
            
            
            String[] teste_ex = {"X14","Yes","No","No","Yes","Some","$$$","No","Yes","French","0-10"};
            
            
            String cur_At = "";
            String value_example = "";
            int ind_example = 0;

            String cur_var = "";
            while (! Decision_tree.isEmpty()){
                String aux = Decision_tree.remove();
                
                //quando recebemos um atributo fazemos o seguinte
                if (aux.contains("Atributo")){
                    cur_At = aux.replaceAll("\\s", "");
                    cur_At = cur_At.substring(8,cur_At.length()); 
                    ind_example = getPos_col(cur_At);

                    //todo criar no e respetivos filhos ()
                    //while (!Decision_tree.peek() .contains("Attribute"))
                    
                    //todo aproveitar este ciclo acima para ir criando os nos
                    
                    //? System.out.println(cur_At);
                    // ? System.out.println(getPos_col(cur_At));        
                }

                //quando recebemos uma variavel
                else if (!aux.contains("Class")){
                    cur_var = aux.replaceAll("\\s", "");
                    cur_var = cur_var.substring(0,cur_var.length()-1);



                    System.out.println(cur_var);
                }
                System.out.println(aux);
            }



            //!Ideia para catogorizar os exemplos:
            //*  ->depois de ler o csv e colocar os dados em examples e attributos
            //*  pegar nas colunas que sao numericas e descobrir o valor maximo e minimo
            //*  e dividir os valores por <= e > e substituir na propria coluna 

        }
            
    
    }
}