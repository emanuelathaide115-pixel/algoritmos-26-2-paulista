import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
    private String cpf;
    private String nome;
    private String telefone;
    private String email;

    public Cliente(String cpf, String nome, String telefone, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }

    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEmail(String email) { this.email = email; }

    public static void cadastrarCliente(Scanner entrada, Cliente[] clientes) {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        int posicao = primeiraPosicaoLivre(clientes);
        if (posicao == -1) {
            System.out.println("Limite de clientes atingido.");
            return;
        }

        String cpf = lerObrigatorio(entrada, "CPF: ");
        cpf = normalizarCpf(cpf);
        if (!cpfValido(cpf)) {
            System.out.println("CPF inválido. Informe 11 dígitos.");
            return;
        }
        if (buscarPorCpf(clientes, cpf) != null) {
            System.out.println("Já existe um cliente com esse CPF.");
            return;
        }

        String nome = lerObrigatorio(entrada, "Nome: ");
        String telefone = lerObrigatorio(entrada, "Telefone: ");
        String email = lerEmail(entrada);

        clientes[posicao] = new Cliente(cpf, nome, telefone, email);
        salvarCSV(clientes);
        System.out.println("Cliente cadastrado com sucesso.");
    }

    public static void listarClientes(Cliente[] clientes) {
        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        boolean encontrou = false;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                exibirCliente(cliente);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum cliente cadastrado.");
        }
    }

    public static void buscarClienteMenu(Scanner entrada, Cliente[] clientes) {
        String cpf = lerObrigatorio(entrada, "CPF do cliente: ");
        Cliente cliente = buscarPorCpf(clientes, cpf);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
        } else {
            exibirCliente(cliente);
        }
    }

    public static void alterarCliente(Scanner entrada, Cliente[] clientes) {
        String cpfAtual = lerObrigatorio(
                entrada, "CPF do cliente que deseja alterar: ");
        cpfAtual = normalizarCpf(cpfAtual);
        Cliente cliente = buscarPorCpf(clientes, cpfAtual);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        String novoCpf = lerObrigatorio(entrada, "Novo CPF: ");
        novoCpf = normalizarCpf(novoCpf);
        if (!cpfValido(novoCpf)) {
            System.out.println("CPF inválido. Informe 11 dígitos.");
            return;
        }
        Cliente existente = buscarPorCpf(clientes, novoCpf);
        if (existente != null && existente != cliente) {
            System.out.println("Esse CPF já pertence a outro cliente.");
            return;
        }

        String novoNome = lerObrigatorio(entrada, "Novo nome: ");
        String novoTelefone = lerObrigatorio(entrada, "Novo telefone: ");
        String novoEmail = lerEmail(entrada);

        cliente.setCpf(novoCpf);
        cliente.setNome(novoNome);
        cliente.setTelefone(novoTelefone);
        cliente.setEmail(novoEmail);
        salvarCSV(clientes);
        System.out.println("Cliente alterado com sucesso.");
    }

    public static void removerCliente(Scanner entrada, Cliente[] clientes) {
        String cpf = lerObrigatorio(entrada, "CPF do cliente: ");
        cpf = normalizarCpf(cpf);
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] != null && clientes[i].getCpf().equals(cpf)) {
                clientes[i] = null;
                salvarCSV(clientes);
                System.out.println("Cliente removido com sucesso.");
                return;
            }
        }
        System.out.println("Cliente não encontrado.");
    }

    public static Cliente buscarPorCpf(Cliente[] clientes, String cpf) {
        cpf = normalizarCpf(cpf);
        for (Cliente cliente : clientes) {
            if (cliente != null && cliente.getCpf().equals(cpf)) {
                return cliente;
            }
        }
        return null;
    }

    public static void salvarCSV(Cliente[] clientes) {
        File arquivo = new File("clientes.csv");
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {
            escritor.write("cpf;nome;telefone;email");
            escritor.newLine();
            for (Cliente cliente : clientes) {
                if (cliente != null) {
                    escritor.write(limparCampo(cliente.getCpf()) + ";"
                            + limparCampo(cliente.getNome()) + ";"
                            + limparCampo(cliente.getTelefone()) + ";"
                            + limparCampo(cliente.getEmail()));
                    escritor.newLine();
                }
            }
        } catch (IOException erro) {
            System.out.println("Erro ao salvar clientes.csv: "
                    + erro.getMessage());
        }
    }

    public static void carregarCSV(Cliente[] clientes) {
        File arquivo = new File("clientes.csv");
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String linha;
            int posicao = 0;
            while ((linha = leitor.readLine()) != null
                    && posicao < clientes.length) {
                if (linha.trim().equalsIgnoreCase(
                        "cpf;nome;telefone;email")) {
                    continue;
                }
                String[] campos = linha.split(";", -1);
                if (campos.length != 4
                        || campos[0].isBlank()
                        || campos[1].isBlank()
                        || campos[2].isBlank()
                        || campos[3].isBlank()) {
                    continue;
                }
                String cpf = normalizarCpf(campos[0]);
                if (!cpfValido(cpf) || buscarPorCpf(clientes, cpf) != null) continue;
                clientes[posicao] = new Cliente(
                        cpf, campos[1], campos[2], campos[3]);
                posicao++;
            }
        } catch (IOException erro) {
            System.out.println("Erro ao carregar clientes.csv: "
                    + erro.getMessage());
        }
    }

    private static int primeiraPosicaoLivre(Cliente[] clientes) {
        for (int i = 0; i < clientes.length; i++) {
            if (clientes[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private static String lerObrigatorio(Scanner entrada, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = entrada.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("Este campo é obrigatório.");
        }
    }

    private static String lerEmail(Scanner entrada) {
        while (true) {
            String email = lerObrigatorio(entrada, "E-mail: ");
            int arroba = email.indexOf('@');
            int ultimoPonto = email.lastIndexOf('.');
            if (arroba > 0 && ultimoPonto > arroba + 1
                    && ultimoPonto < email.length() - 1) {
                return email;
            }
            System.out.println("Digite um e-mail válido.");
        }
    }

    private static void exibirCliente(Cliente cliente) {
        System.out.println("CPF: " + cliente.getCpf());
        System.out.println("Nome: " + cliente.getNome());
        System.out.println("Telefone: " + cliente.getTelefone());
        System.out.println("E-mail: " + cliente.getEmail());
        System.out.println("------------------------------");
    }

    private static String limparCampo(String texto) {
        return texto.replace(";", ",")
                .replace("\n", " ")
                .replace("\r", " ");
    }

    private static String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private static boolean cpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.matches("(\\d)\\1{10}")) return false;
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int digito = 11 - soma % 11;
        if (digito >= 10) digito = 0;
        if (digito != cpf.charAt(9) - '0') return false;
        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        digito = 11 - soma % 11;
        if (digito >= 10) digito = 0;
        return digito == cpf.charAt(10) - '0';
    }
}

