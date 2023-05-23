import java.util.*;
import java.io.*;

public class teste {
    static ArrayList<String> Attributes = new ArrayList<>();            //Guarda os atributos 

    static ArrayList<String[]> Examples = new ArrayList<>();            //Guarda os exemplos 

    static int Example_size;                                            //Tamanho de cada exemplo

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
        System.out.println(Arrays.toString(n));
        return n;

    }
    public static void Read_csv(String name) throws Exception{
        File file1 = new File(name);
        Scanner sc = new Scanner(file1);

        //Leitura e armazenamento de Atributos        
        String[] aux = sc.nextLine().split(",");

        for (String attribute : aux)
            Attributes.add(attribute);
        
        System.out.println("Atributos :" + Attributes );

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
        for (String[] example : Examples){
            System.out.println("Exemplo " + i + ": " + Arrays.toString(example));
            i++;
        } 
    }

    //verifica se existem campos inteiros para descritizar
    public static int[] int_to_discretiz(){
        String[] aux = Examples.get(0);
        int i = 0;
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
        //todo criar array com indices a discretizars(valores de i)
        int ans[] = new int[ind.size()];
        i = 0;
        for (int v : ind){
            ans[i] = v;
            i++;
        }

        return ans;
    }

    //procura o maior e o menor valor dos  exemplos, na coluna indice
    public static int[] Max_Min_Value(int indice){
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

    //Discretiza as variaveis
    public static void discretiz(){
        //guarda as colunas que podem ser descritizadas
        int [] int_ind_to_discratize = int_to_discretiz();


        for (int indice : int_ind_to_discratize){
            //para cada coluna em indice 
            //eu quero discretizar ()
            int [] Min_Max = Max_Min_Value(indice);
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
        // System.out.println(Arrays.toString(int_ind_to_discratize));

        //discretizar inteiros e double em metodo sperados


    }

    public static void main(String[] args) throws Exception{
        if (args[0].contains(".csv")){
            Read_csv(args[0]);
            
        discretiz();
        for (String []e : Examples){
            System.out.println(Arrays.toString(e));
        }
    }
}
}
