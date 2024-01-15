package dev.erikmota.desafiounikamain;

public class Test {
    public static void main(String[] args) {
        validar(1);
        //validar(2);
        //validar(4);
    }

    public static void validar(int tamanho){
        for (int i = tamanho-1; i >= 1; i--){
            for (int j = i-1; j >= 0; j--){
                System.out.println("I: " + i + "| J: " + j);
            }
        }
    }
}

