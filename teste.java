import java.util.*;
public class teste {
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

    public static void main(String[] args) {
        // String[] s = { "s0" , "s1", "s2" ,"s3"};
        // System.out.println("Antes : " + Arrays.toString(s));
        // s = remove(s,3);
        // System.out.println("Depois : " + Arrays.toString(s));
        ArrayList<String> s = new ArrayList<>();
        String aux = "ola como estas";
        s.add(aux);
        // ArrayList<String> t = s;
        ArrayList <String> t = new ArrayList<>(s);
        s.remove(0);
        System.out.println("s -> " + s);
        System.out.println("t -> " + t);
        


    }
}
