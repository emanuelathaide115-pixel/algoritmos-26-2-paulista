import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Venda {
    private static final Path ARQUIVO = Path.of("vendas.csv");
    private static final String CABECALHO = "data_hora;cliente;itens;total";

    private Venda() { }

    public static void registrar(String cliente, String itens, double total) {
        boolean escreverCabecalho = !Files.exists(ARQUIVO);
        try {
            escreverCabecalho = escreverCabecalho || Files.size(ARQUIVO) == 0;
            try (BufferedWriter escritor = Files.newBufferedWriter(
                    ARQUIVO, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (escreverCabecalho) {
                    escritor.write(CABECALHO);
                    escritor.newLine();
                }
                String dataHora = LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                escritor.write(limparCampo(dataHora) + ";"
                        + limparCampo(cliente) + ";"
                        + limparCampo(itens) + ";"
                        + String.format(Locale.US, "%.2f", total));
                escritor.newLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao registrar a venda: " + erro.getMessage());
        }
    }

    public static void listar() {
        System.out.println("\n=== HISTÓRICO DE VENDAS ===");
        if (!Files.exists(ARQUIVO)) {
            System.out.println("Nenhuma venda registrada.");
            return;
        }

        int quantidade = 0;
        double faturamento = 0.0;
        try (BufferedReader leitor = Files.newBufferedReader(
                ARQUIVO, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().equalsIgnoreCase(CABECALHO)) continue;
                String[] campos = linha.split(";", 4);
                if (campos.length != 4) continue;
                try {
                    double total = Double.parseDouble(campos[3]);
                    System.out.println("Data/hora: " + campos[0]);
                    System.out.println("Cliente: " + campos[1]);
                    System.out.println("Itens: " + campos[2]);
                    System.out.printf(Locale.forLanguageTag("pt-BR"),
                            "Total: R$ %.2f%n", total);
                    System.out.println("------------------------------");
                    faturamento += total;
                    quantidade++;
                } catch (NumberFormatException erro) {
                    System.out.println("Registro inválido ignorado em vendas.csv.");
                }
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler vendas.csv: " + erro.getMessage());
            return;
        }

        if (quantidade == 0) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            System.out.println("Vendas registradas: " + quantidade);
            System.out.printf(Locale.forLanguageTag("pt-BR"),
                    "Faturamento acumulado: R$ %.2f%n", faturamento);
        }
    }

    private static String limparCampo(String valor) {
        return valor.replace(";", ",")
                .replace("\n", " / ")
                .replace("\r", " ");
    }
}

