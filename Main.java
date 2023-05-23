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
            if (A == null){
                // for (String[] e : t.examples){
                    // System.out.println(Arrays.toString(e));
                // }
                for (Coluna c : t.colunas){
                    System.out.println("->" + c.nome + t.colunas.length);
                }
                
            }
            System.out.println(Ident+"Atributo " + A );
            

            root = new ROOTNode(A,t.colunas,examples,attributes);
            // ? System.out.println(root.var_class_ind);
      
            //*Fim  Best splitting attribute */

            // System.out.println(attributes);
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

                        //!Cria o filho de root para esse "caso especial"


                        //todo VERIFICAR SE NAO CAUSA ERROS NA GERACAO DA ARVORE DE DECISAO
                        ROOTNode aux = new ROOTNode(v,0);
                        aux.filhos.add(new ROOTNode(Most_Common(examples)));
                        root.filhos.add(aux);

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
    
    //verifica se existem campos inteiros para descritizar
    public static int[] int_to_discretiz(){
        String[] aux = Examples.get(0);
        int i = 0;
        //guarda os indices das colunas que contem variaveis inteiras
        ArrayList<Integer> ind = new ArrayList<>();
        boolean cond = true;
        for (String s : aux ){
            cond = true;
            if (i != 0){
                // System.out.println(s );
                try{
                    Integer.valueOf(s); 
                }catch(NumberFormatException e){
                    cond = false;
                }
                if (cond == true){
                    ind.add(i);
                    // System.out.println(s + "->" + i);
                }
            }
            i++;
        }
        // coloca os indices das colunas que e possivel fazer descritazacao
        int ans[] = new int[ind.size()];
        i = 0;
        for (int v : ind){
            ans[i] = v;
            i++;
        }
        return ans;
    }


    //procura o maior e o menor valor inteiro dos  exemplos, na coluna indice
    public static int[] Max_Min_Value_INT(int indice){
        //ans[0] = min , ans[1] = max
        int[] ans = new int[2];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (String [] e : Examples){
            int cur = Integer.valueOf(e[indice]);
            min = Math.min(min,cur);
            max = Math.max(max,cur);
        }
        //ans[0] = min , ans[1] = max
        ans [0] = min;
        ans [1] = max;
        return ans;
    }

    //procura o maior e o menor valor dos  exemplos, na coluna indice
    public static double[] Max_Min_Value_DOUBLE(int indice){
        //ans[0] = min , ans[1] = max
        double[] ans = new double[2];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (String [] e : Examples){
            double cur = Double.valueOf(e[indice]);
            min = Math.min(min,cur);
            max = Math.max(max,cur);
        }
        //ans[0] = min , ans[1] = max
        ans [0] = min;
        ans [1] = max;
        return ans;
    
    }
    //verifica se existem campos com virgula para descritizar
    public static int[] double_to_discretiz(){
        String[] aux = Examples.get(0);
        int i = 0;
        ArrayList<Integer> ind = new ArrayList<>();
        boolean cond = true;
        for (String s : aux ){
            cond = true;
            if (i != 0){
                // System.out.println(s );
                try{
                    Double.valueOf(s); 
                }catch(NumberFormatException e){
                    cond = false;
                }
                if (cond == true){
                    ind.add(i);
                    // System.out.println(s + "->" + i);
                }
            }
            i++;
        }
        //todo criar array com indices a discretizars(valores de i)
        int ans[] = new int[ind.size()];
        i = 0;
        for (int v : ind){
            ans[i] = v;
            i++;
        }

        return ans;
    }
    //Discretiza as variaveis inteiras e com virgula
    public static void discretize(){
        //guarda as colunas que contem inteiros que podem ser descritizadas
        int [] int_ind_to_discratize = int_to_discretiz();

        for (int indice : int_ind_to_discratize){
            //para cada coluna em indice 
            //eu quero discretizar ()
            int [] Min_Max = Max_Min_Value_INT(indice);
            int middle = (Min_Max[1] + Min_Max[0])/2;

            //fazemos a divisiao em cada exemplo em Example
            // de <= middle e > middle 
            for (String[] e : Examples){
                if (Integer.valueOf(e[indice]) <= middle)
                    e[indice] = "<="+middle;
                else 
                    e[indice] = ">"+middle;
            }
        }

        //guarda as colunas que podem ser descritazadas
        int [] double_int_to_discretize = double_to_discretiz();
        // System.out.println(Arrays.toString(double_int_to_discretize));

        for (int indice : double_int_to_discretize){
            //para cada coluna em indice 
            //eu quero discretizar 
            double[] Min_Max = Max_Min_Value_DOUBLE(indice);
            double middle = (Min_Max[1] + Min_Max[0])/2;
            //fazemos a divisiao em cada exemplo em Example
            // de <= middle e > middle 
            for (String[] e : Examples){
                if (Double.valueOf(e[indice]) <= middle)
                    e[indice] = "<="+middle;
                else 
                    e[indice] = ">"+middle;
            }
        }


    }


    public static void main(String[] args) throws Exception{
        //Para criar uma arvore de decisao dado o csv
        if (args[0].contains(".csv")){
            Read_csv(args[0]);

            get_Classes();

            //Discretiza se for possivel
            discretize();

            Inialtilize_atributes_var();
            



            //* Imprimimos informacao necessaria para adicionar um exemplo (nr de atributos e atributos em si)
            System.out.print (Attributes.size() + " ");
            for (String atr : Attributes)
                System.out.print(atr + " ");
            System.out.println();

            // todo  tentar fazer o que esta descrito abaixo
            //*  ->depois de ler o csv e colocar os dados em examples e attributos
            //*  pegar nas colunas que sao numericas e descobrir o valor maximo e minimo
            //*  e dividir os valores por <= e > e substituir na propria coluna 

            //*criamos a arvore de decisao
            ROOTNode n = ID3 (new ArrayList<String[]> (Examples),Attributes.get(Attributes.size()-1),new ArrayList<String> (Attributes),"");


            //*Categorizacao de  um exemplo
            //todo Acabar a parte de categorizar um exemplo
            
            /* 
            String[] teste_ex = {"X14","Yes","No","Yes","Yes","Full","$$$","No","Yes","Thai","0-10"};
            
            //Ciclo que percorre a arvore de decisao para categorizar um novo exemplo
            ROOTNode cur = n;
            int ind = getPos_col(cur.name_col);            
            Attributes.remove(ind);
            int i =0;

            outerloop:
            while (true){

                if (cur.name_col != null)
                    System.out.println("i : " + i + " = "+  cur.name_col); 
                if (cur.name_var != null)
                    System.out.println("i : " + i + " = "+  cur.name_var);      
                   
                for (ROOTNode f : cur.filhos){
                    if (f.name_var != null){
                        // if (i == 5){
                            //System.out.printf("var = %s e teste[%d] = %s\n",f.name_var,i,teste_ex[ind]);
                        // }
                        if (f.name_var.equals(teste_ex[ind])){
                            System.out.printf("nosso exemplo na coluna %s  = %s\n",cur.name_col,teste_ex[ind]);

                            //Escolhemos o respetivo filho na arvore de decisao
                            cur = f;

                            //Atualizamos teste exemplo (removendo o argumento que encontramos)
                            teste_ex = remove(teste_ex, ind);

                            break;
                        }
                    }
                    else if (Classes.contains(f.name_col)){
                        System.out.printf("exemplo e categorizado como: %s\n",f.name_col);
                        break outerloop;
                    }
                    else if (f.name_col != null){
                        //?System.out.println(f.name_col);
                        
                        //Escolhemos o respetivo filho na arvore de decisao
                        cur = f;
                        
                        

                        //Atualizamos o indice 
                        ind = getPos_col(f.name_col);
                        
                        //Removemos o atributo nesse indice
                        Attributes.remove(ind);
                        
                        break;
                        
                    }
                    
                }  
                //Serve ajudar a ver os "passos" que o 
                // nosso exemplo segue na arvore de decisao 
                i++;
                if (i == 8) break;
            }
            */
        }
                
    }
}