package util;

import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

public class TratamentoDeExcecoes{
    public static void erroCasaDecimal(InputMismatchException e){
        System.out.println("Erro de entrada: A mascara para o campo é: x.xxx,xx ou xxxx,xx");
    }
    public static void erroCaracterInvalido(InputMismatchException e){
        System.out.println("Erro de entrada: Digitar somente números inteiros");
    }
    public static void erroDataValidade(DateTimeParseException e){
        System.out.println("Erro de entrada: Digitar somente números de acordo com a mascara: yyyy-MM-dd");
    }
}
