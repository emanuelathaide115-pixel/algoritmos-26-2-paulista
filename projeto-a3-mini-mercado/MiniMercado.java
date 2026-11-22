import java.util.Scanner;

public class MiniMercado {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        int tamanho = 100;
        Cliente[] clientes = new Cliente[tamanho];
        Produto[] produtos = new Produto[tamanho];

        Cliente.carregarCSV(clientes);
        Produto.carregarCSV(produtos);

        int opcao = -1;

        do {
            System.out.println("\n====================================");
            System.out.println("           MINI MERCADO             ");
            System.out.println("====================================");
            System.out.println("1 - Produtos");
            System.out.println("2 - Clientes");
            System.out.println("3 - Realizar Compra");
            System.out.println("4 - Controle de Estoque");
            System.out.println("5 - Histórico de Vendas");
            System.out.println("6 - Relatório de Estoque Baixo");
            System.out.println("7 - Resumo Geral do Estoque");
            System.out.println("0 - Sair");
            System.out.println("====================================");
            System.out.print("Escolha uma opção: ");

            if (entrada.hasNextInt()) {
                opcao = entrada.nextInt();
                entrada.nextLine();
            } else {
                System.out.println("Erro: Digite apenas números válidos.");
                entrada.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    menuProdutos(entrada, produtos);
                    break;
                case 2:
                    menuClientes(entrada, clientes);
                    break;
                case 3:
                    Produto.realizarCompra(entrada, produtos, clientes);
                    break;
                case 4:
                    menuEstoque(entrada, produtos);
                    break;
                case 5:
                    Venda.listar();
                    break;
                case 6:
                    Produto.relatorioEstoqueBaixo(entrada, produtos);
                    break;
                case 7:
                    Produto.relatorioResumoEstoque(produtos);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 0);

        entrada.close();
    }

    public static void menuProdutos(Scanner entrada, Produto[] produtos) {
        int opcao = -1;

        do {
            System.out.println("\n====================================");
            System.out.println("         GESTÃO DE PRODUTOS         ");
            System.out.println("====================================");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("3 - Buscar Produto");
            System.out.println("4 - Alterar Produto");
            System.out.println("5 - Remover Produto");
            System.out.println("0 - Voltar ao Menu Principal");
            System.out.println("====================================");
            System.out.print("Escolha uma opção: ");

            if (entrada.hasNextInt()) {
                opcao = entrada.nextInt();
                entrada.nextLine();
            } else {
                System.out.println("Erro: Digite apenas números.");
                entrada.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    Produto.cadastrarProduto(entrada, produtos);
                    break;
                case 2:
                    Produto.listarProdutos(produtos);
                    break;
                case 3:
                    Produto.buscarProdutoMenu(entrada, produtos);
                    break;
                case 4:
                    Produto.alterarProduto(entrada, produtos);
                    break;
                case 5:
                    Produto.removerProduto(entrada, produtos);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    public static void menuClientes(Scanner entrada, Cliente[] clientes) {
        int opcao = -1;

        do {
            System.out.println("\n====================================");
            System.out.println("         GESTÃO DE CLIENTES         ");
            System.out.println("====================================");
            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Buscar Cliente");
            System.out.println("4 - Alterar Cliente");
            System.out.println("5 - Remover Cliente");
            System.out.println("0 - Voltar ao Menu Principal");
            System.out.println("====================================");
            System.out.print("Escolha uma opção: ");

            if (entrada.hasNextInt()) {
                opcao = entrada.nextInt();
                entrada.nextLine();
            } else {
                System.out.println("Erro: Digite apenas números.");
                entrada.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    Cliente.cadastrarCliente(entrada, clientes);
                    break;
                case 2:
                    Cliente.listarClientes(clientes);
                    break;
                case 3:
                    Cliente.buscarClienteMenu(entrada, clientes);
                    break;
                case 4:
                    Cliente.alterarCliente(entrada, clientes);
                    break;
                case 5:
                    Cliente.removerCliente(entrada, clientes);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    public static void menuEstoque(Scanner entrada, Produto[] produtos) {
        int opcao = -1;
        do {
            System.out.println("\n====================================");
            System.out.println("        CONTROLE DE ESTOQUE         ");
            System.out.println("====================================");
            System.out.println("1 - Consultar estoque");
            System.out.println("2 - Repor estoque");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            if (!entrada.hasNextInt()) {
                entrada.nextLine();
                System.out.println("Digite uma opção numérica.");
                continue;
            }
            opcao = entrada.nextInt();
            entrada.nextLine();
            if (opcao == 1) {
                Produto.listarProdutos(produtos);
            } else if (opcao == 2) {
                Produto.listarProdutos(produtos);
                System.out.print("Código do produto para reposição: ");
                if (!entrada.hasNextInt()) {
                    entrada.nextLine();
                    System.out.println("Código inválido.");
                    continue;
                }
                int codigo = entrada.nextInt();
                entrada.nextLine();
                int indice = Produto.buscarIndicePorCodigo(produtos, codigo);
                if (indice < 0) {
                    System.out.println("Produto não encontrado.");
                    continue;
                }
                System.out.print("Quantidade a adicionar: ");
                if (!entrada.hasNextInt()) {
                    entrada.nextLine();
                    System.out.println("Quantidade inválida.");
                    continue;
                }
                int quantidade = entrada.nextInt();
                entrada.nextLine();
                if (quantidade <= 0 || produtos[indice].getEstoque() > Integer.MAX_VALUE - quantidade) {
                    System.out.println("Informe uma quantidade positiva válida.");
                    continue;
                }
                produtos[indice].setEstoque(produtos[indice].getEstoque() + quantidade);
                Produto.salvarCSV(produtos);
                System.out.println("Estoque atualizado com sucesso.");
            } else if (opcao != 0) {
                System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}

