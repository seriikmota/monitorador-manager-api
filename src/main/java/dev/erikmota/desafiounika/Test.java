package dev.erikmota.desafiounika;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Monitorador m = new Monitorador(TipoPessoa.FISICA, null, null, null,
                "710.611.701-36", "Erik de Sousa Mota", "746", LocalDate.of(2020,2,2), "erik@gmail.com", true);

        System.out.println(m.getData());
    }
}

