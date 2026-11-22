import java.io.*;
import java.util.Scanner;

public class Produto {
    private int codigo;
    private String nome;
    private double preco;
    private int estoque;

    public Produto(int codigo, String nome, double preco, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    // 1 - Cadastrar Produto
    public static void cadastrarProduto(Scanner entrada, Produto[] produtos) {
        System.out.println("\n=== CADASTRAR PRODUTO ===");
        int posLivre = -1;
        for (int i = 0; i < produtos.length; i++) {
            if (produtos[i] == null) {
                posLivre = i;
                break;
            }
        }

        if (posLivre == -1) {
            System.out.println("Limite máximo de produtos atingido!");
            return;
        }

        System.out.print("Código do produto: ");
        if (!entrada.hasNextInt()) {
            entrada.nextLine();
            System.out.println("Código inválido.");
            return;
        }
        int codigo = entrada.nextInt();
        entrada.nextLine();
        if (codigo <= 0) {
            System.out.println("O código deve ser positivo.");
            return;
        }

        if (buscarIndicePorCodigo(produtos, codigo) != -1) {
            System.out.println("Erro: Já existe um produto com este código!");
            return;
        }

        System.out.print("Nome do produto: ");
        String nome = entrada.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("O nome é obrigatório.");
            return;
        }

        System.out.print("Preço (ex: 5,50 ou 5.50): ");
        String precoStr = entrada.nextLine().replace(",", ".");
        double preco;
        try {
            preco = Double.parseDouble(precoStr);
            if (!Double.isFinite(preco) || preco <= 0) throw new NumberFormatException();
        } catch (NumberFormatException erro) {
            System.out.println("Preço inválido. Informe um valor maior que zero.");
            return;
        }

        System.out.print("Quantidade em estoque: ");
        if (!entrada.hasNextInt()) {
            entrada.nextLine();
            System.out.println("Estoque inválido.");
            return;
        }
        int estoque = entrada.nextInt();
        entrada.nextLine();
        if (estoque < 0) {
            System.out.println("O estoque não pode ser negativo.");
            return;
        }

        produtos[posLivre] = new Produto(codigo, nome, preco, estoque);
        salvarCSV(produtos);
        System.out.println("Produto cadastrado com sucesso!");
    }

    // 2 - Listar Produtos
    public static void listarProdutos(Produto[] produtos) {
        System.out.println("\n=== LISTA DE PRODUTOS ===");
        boolean vazio = true;
        for (int i = 0; i < produtos.length; i++) {
            if (produtos[i] != null) {
                System.out.printf("Cód: %d | Nome: %s | Preço: R$ %.2f | Estoque: %d%n",
                        produtos[i].getCodigo(), produtos[i].getNome(),
                        produtos[i].getPreco(), produtos[i].getEstoque());
                vazio = false;
            }
        }
        if (vazio) {
            System.out.println("Nenhum produto cadastrado.");
        }
    }

    public static void relatorioEstoqueBaixo(Scanner entrada, Produto[] produtos) {
        System.out.print("Limite de estoque para o alerta (padrão 5): ");
        String textoLimite = entrada.nextLine().trim();
        int limite = 5;
        if (!textoLimite.isEmpty()) {
            try {
                limite = Integer.parseInt(textoLimite);
                if (limite < 0) throw new NumberFormatException();
            } catch (NumberFormatException erro) {
                System.out.println("Limite inválido. Informe um número inteiro não negativo.");
                return;
            }
        }

        System.out.println("\n=== RELATÓRIO DE ESTOQUE BAIXO ===");
        System.out.println("Produtos com estoque igual ou menor que " + limite + ":");
        boolean encontrou = false;
        for (Produto produto : produtos) {
            if (produto != null && produto.getEstoque() <= limite) {
                String situacao = produto.getEstoque() == 0 ? "SEM ESTOQUE" : "REPOR";
                System.out.printf("Cód: %d | %s | Estoque: %d | %s%n",
                        produto.getCodigo(), produto.getNome(),
                        produto.getEstoque(), situacao);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum produto está abaixo desse limite.");
        }
    }

    /** Exibe resumo quantitativo e financeiro do estoque atual. */
    public static void relatorioResumoEstoque(Produto[] produtos) {
        int quantidadeProdutos = 0;
        long totalUnidades = 0;
        double valorEstoque = 0.0;
        for (Produto produto : produtos) {
            if (produto != null) {
                quantidadeProdutos++;
                totalUnidades += produto.getEstoque();
                valorEstoque += produto.getPreco() * produto.getEstoque();
            }
        }
        System.out.println("\n=== RESUMO GERAL DO ESTOQUE ===");
        System.out.println("Produtos cadastrados: " + quantidadeProdutos);
        System.out.println("Unidades em estoque: " + totalUnidades);
        System.out.printf("Valor estimado a preço de venda: R$ %.2f%n", valorEstoque);
    }

    // 3 - Buscar Produto
    public static void buscarProdutoMenu(Scanner entrada, Produto[] produtos) {
        System.out.println("\n=== BUSCAR PRODUTO ===");
        System.out.print("Digite o código do produto: ");
        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); return; }
        int codigo = entrada.nextInt();
        entrada.nextLine();

        int idx = buscarIndicePorCodigo(produtos, codigo);
        if (idx != -1) {
            System.out.printf("Encontrado -> Cód: %d | Nome: %s | Preço: R$ %.2f | Estoque: %d%n",
                    produtos[idx].getCodigo(), produtos[idx].getNome(),
                    produtos[idx].getPreco(), produtos[idx].getEstoque());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    // 4 - Alterar Produto
    public static void alterarProduto(Scanner entrada, Produto[] produtos) {
        System.out.println("\n=== ALTERAR PRODUTO ===");
        System.out.print("Digite o código do produto a alterar: ");
        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); return; }
        int codigo = entrada.nextInt();
        entrada.nextLine();

        int idx = buscarIndicePorCodigo(produtos, codigo);
        if (idx == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.print("Novo nome (" + produtos[idx].getNome() + "): ");
        String nome = entrada.nextLine();
        String novoNome = nome.trim().isEmpty() ? produtos[idx].getNome() : nome.trim();

        System.out.print("Novo preço (atual: " + produtos[idx].getPreco() + "): ");
        String precoStr = entrada.nextLine().trim().replace(",", ".");
        double novoPreco = produtos[idx].getPreco();
        if (!precoStr.trim().isEmpty()) {
            try {
                novoPreco = Double.parseDouble(precoStr);
                if (!Double.isFinite(novoPreco) || novoPreco <= 0) throw new NumberFormatException();
            } catch (NumberFormatException erro) {
                System.out.println("Preço inválido; alteração cancelada.");
                return;
            }
        }

        System.out.print("Novo estoque (atual: " + produtos[idx].getEstoque() + "): ");
        String estStr = entrada.nextLine().trim();
        int novoEstoque = produtos[idx].getEstoque();
        if (!estStr.trim().isEmpty()) {
            try {
                novoEstoque = Integer.parseInt(estStr);
                if (novoEstoque < 0) throw new NumberFormatException();
            } catch (NumberFormatException erro) {
                System.out.println("Estoque inválido; alteração cancelada.");
                return;
            }
        }

        produtos[idx].setNome(novoNome);
        produtos[idx].setPreco(novoPreco);
        produtos[idx].setEstoque(novoEstoque);
        salvarCSV(produtos);
        System.out.println("Produto alterado com sucesso!");
    }

    // 5 - Remover Produto
    public static void removerProduto(Scanner entrada, Produto[] produtos) {
        System.out.println("\n=== REMOVER PRODUTO ===");
        System.out.print("Digite o código do produto a remover: ");
        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); return; }
        int codigo = entrada.nextInt();
        entrada.nextLine();

        int idx = buscarIndicePorCodigo(produtos, codigo);
        if (idx == -1) {
            System.out.println("Produto não encontrado.");
            return;
        }

        produtos[idx] = null;
        salvarCSV(produtos);
        System.out.println("Produto removido com sucesso!");
    }

    // Fluxo de Compra
    public static void realizarCompra(Scanner entrada, Produto[] produtos, Cliente[] clientes) {
        System.out.println("\n====================================");
        System.out.println("          REALIZAR COMPRA           ");
        System.out.println("====================================");

        System.out.print("O cliente já possui cadastro? (S/N): ");
        String resp = entrada.nextLine().trim();
        String nomeCliente = "Consumidor Final";

        if (resp.equalsIgnoreCase("S")) {
            System.out.print("Informe o CPF do cliente: ");
            String cpf = entrada.nextLine().trim();
            Cliente c = Cliente.buscarPorCpf(clientes, cpf);
            if (c != null) {
                nomeCliente = c.getNome();
                System.out.println("Cliente identificado: " + nomeCliente);
            } else {
                System.out.println("CPF não encontrado. Compra cancelada.");
                return;
            }
        }

        Sacola sacola = new Sacola();
        boolean finalizar = false;
        while (!finalizar) {
            System.out.println("\n--- SACOLA DE COMPRAS ---");
            System.out.println("1 - Adicionar produto");
            System.out.println("2 - Ver sacola");
            System.out.println("3 - Alterar quantidade de um item");
            System.out.println("4 - Remover produto da sacola");
            System.out.println("5 - Finalizar compra");
            System.out.println("0 - Cancelar compra");
            System.out.print("Escolha: ");
            if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Opção inválida."); continue; }
            int opcao = entrada.nextInt();
            entrada.nextLine();

            switch (opcao) {
                case 1:
                    listarProdutos(produtos);
                    System.out.print("Código do produto (0 para voltar): ");
                    if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); break; }
                    int codigo = entrada.nextInt();
                    entrada.nextLine();
                    if (codigo == 0) break;
                    int indice = buscarIndicePorCodigo(produtos, codigo);
                    if (indice == -1) { System.out.println("Produto não encontrado."); break; }
                    Produto produto = produtos[indice];
                    System.out.print("Quantidade (disponível: " + produto.getEstoque() + "): ");
                    if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Quantidade inválida."); break; }
                    int quantidade = entrada.nextInt();
                    entrada.nextLine();
                    if (sacola.adicionar(produto, quantidade)) {
                        System.out.println("Produto adicionado à sacola.");
                    } else {
                        System.out.println("Quantidade inválida, estoque insuficiente ou sacola cheia.");
                    }
                    break;
                case 2:
                    sacola.exibir();
                    break;
                case 3:
                    sacola.exibir();
                    if (!sacola.estaVazia()) {
                        System.out.print("Código do produto para alterar: ");
                        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); break; }
                        int codigoAlterar = entrada.nextInt();
                        entrada.nextLine();
                        System.out.print("Nova quantidade: ");
                        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Quantidade inválida."); break; }
                        int novaQuantidade = entrada.nextInt();
                        entrada.nextLine();
                        System.out.println(sacola.atualizarQuantidade(codigoAlterar, novaQuantidade)
                                ? "Quantidade atualizada." : "Item não encontrado ou quantidade fora do estoque disponível.");
                    }
                    break;
                case 4:
                    sacola.exibir();
                    if (!sacola.estaVazia()) {
                        System.out.print("Código do produto que deseja remover: ");
                        if (!entrada.hasNextInt()) { entrada.nextLine(); System.out.println("Código inválido."); break; }
                        int codigoRemover = entrada.nextInt();
                        entrada.nextLine();
                        System.out.println(sacola.remover(codigoRemover)
                                ? "Produto removido da sacola." : "Produto não está na sacola.");
                    }
                    break;
                case 5:
                    if (sacola.estaVazia()) { System.out.println("Adicione pelo menos um produto."); break; }
                    if (!sacola.concluir()) { System.out.println("Estoque alterado; revise a sacola antes de finalizar."); break; }
                    double subtotalCompra = sacola.getTotal();
                    double desconto = Promocao.solicitarDesconto(entrada, subtotalCompra);
                    double totalCompra = subtotalCompra - desconto;
                    String resumoItens = sacola.resumo();
                    if (desconto > 0) {
                        resumoItens += String.format("- Desconto promocional: -R$ %.2f%n", desconto);
                    }
                    salvarCSV(produtos);
                    System.out.println("\n====================================");
                    System.out.println("         RESUMO DA COMPRA           ");
                    System.out.println("====================================");
                    System.out.println("Cliente: " + nomeCliente);
                    System.out.print(resumoItens);
                    if (desconto > 0) System.out.printf("Subtotal: R$ %.2f%n", subtotalCompra);
                    System.out.println("------------------------------------");
                    System.out.printf("TOTAL A PAGAR: R$ %.2f%n", totalCompra);
                    System.out.println("====================================");
                    Venda.registrar(nomeCliente, resumoItens, totalCompra);
                    finalizar = true;
                    break;
                case 0:
                    System.out.println("Compra cancelada. O estoque não foi alterado.");
                    finalizar = true;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public static int buscarIndicePorCodigo(Produto[] produtos, int codigo) {
        for (int i = 0; i < produtos.length; i++) {
            if (produtos[i] != null && produtos[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    public static void salvarCSV(Produto[] produtos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("produtos.csv"))) {
            writer.println("codigo;nome;preco;estoque");
            for (Produto produto : produtos) {
                if (produto != null) {
                    writer.println(produto.getCodigo() + ";" + limparCampoCSV(produto.getNome())
                            + ";" + produto.getPreco()
                            + ";" + produto.getEstoque());
                }
            }
        } catch (IOException erro) {
            System.out.println("Erro ao salvar produtos.csv: " + erro.getMessage());
        }
    }

    private static String limparCampoCSV(String valor) {
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    public static void carregarCSV(Produto[] produtos) {
        File arquivo = new File("produtos.csv");

        if (!arquivo.exists()) {
            Produto[] exemplos = {
                    new Produto(1, "Arroz", 25.90, 20),
                    new Produto(2, "Feijão", 8.50, 30),
                    new Produto(3, "Leite", 5.20, 24)
            };

            int limite = Math.min(produtos.length, exemplos.length);
            for (int i = 0; i < limite; i++) {
                produtos[i] = exemplos[i];
            }
            salvarCSV(produtos);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int indice = 0;
            while ((linha = reader.readLine()) != null
                    && indice < produtos.length) {
                if (linha.trim().equalsIgnoreCase("codigo;nome;preco;estoque")) continue;
                String[] partes = linha.split(";", -1);
                if (partes.length != 4) {
                    continue;
                }

                try {
                    int codigo = Integer.parseInt(partes[0]);
                    String nome = partes[1];
                    double preco = Double.parseDouble(partes[2].replace(",", "."));
                    int estoque = Integer.parseInt(partes[3]);
                    if (codigo <= 0 || nome.isBlank() || !Double.isFinite(preco)
                            || preco <= 0 || estoque < 0
                            || buscarIndicePorCodigo(produtos, codigo) != -1) continue;
                    produtos[indice] = new Produto(codigo, nome, preco, estoque);
                    indice++;
                } catch (NumberFormatException erro) {
                    System.out.println("Linha inválida ignorada em produtos.csv.");
                }
            }
        } catch (IOException erro) {
            System.out.println("Erro ao carregar produtos.csv: " + erro.getMessage());
        }
    }
}

