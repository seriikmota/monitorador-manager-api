package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        List<Monitorador> monitoradors = new ArrayList<>();
        monitoradors.add(new Monitorador(TipoPessoa.FISICA, null, null, null,
                "710.611.701-36", "Erik de Sousa Mota", "746", LocalDate.of(2020,2,2), "erik@gmail.com", true));
        pegarTipo(monitoradors);
    }

    private static void pegarTipo(List<?> lista) {
        if (lista.get(0).getClass() != Monitorador.class){
            System.out.println("asdasd");
        }
        System.out.println(lista.get(0).getClass());
    }
}
