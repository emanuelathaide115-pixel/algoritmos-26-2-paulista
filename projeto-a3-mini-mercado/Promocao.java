import java.util.Locale;
import java.util.Scanner;

/** Cupons promocionais disponíveis no Mini Mercado. */
public final class Promocao {
    private Promocao() { }

    /** Retorna o percentual do cupom ou zero quando o código não existe. */
    public static double percentualDoCupom(String codigo) {
        if (codigo == null) return 0.0;
        switch (codigo.trim().toUpperCase(Locale.ROOT)) {
            case "MERCADO5": return 0.05;
            case "CLIENTE10": return 0.10;
            default: return 0.0;
        }
    }

    /** Pergunta por um cupom opcional e retorna o valor do desconto. */
    public static double solicitarDesconto(Scanner entrada, double subtotal) {
        System.out.print("Cupom de desconto (Enter para continuar sem cupom): ");
        String codigo = entrada.nextLine().trim();
        if (codigo.isEmpty()) return 0.0;

        double percentual = percentualDoCupom(codigo);
        if (percentual == 0.0) {
            System.out.println("Cupom inválido. Compra seguirá sem desconto.");
            return 0.0;
        }

        double desconto = Math.round(subtotal * percentual * 100.0) / 100.0;
        System.out.printf("Cupom aplicado: %.0f%% de desconto (R$ %.2f).%n",
                percentual * 100.0, desconto);
        return desconto;
    }
}

