/** Sacola de compras em memória; o estoque só é alterado na confirmação. */
public class Sacola {
    private final Produto[] produtos = new Produto[100];
    private final int[] quantidades = new int[100];
    private int quantidadeItens;

    public boolean adicionar(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return false;
        int posicao = localizar(produto.getCodigo());
        int naSacola = posicao == -1 ? 0 : quantidades[posicao];
        if (quantidade > produto.getEstoque() - naSacola) return false;

        if (posicao == -1) {
            if (quantidadeItens == produtos.length) return false;
            posicao = quantidadeItens++;
            produtos[posicao] = produto;
        }
        quantidades[posicao] += quantidade;
        return true;
    }

    public boolean remover(int codigo) {
        int posicao = localizar(codigo);
        if (posicao == -1) return false;
        for (int i = posicao; i < quantidadeItens - 1; i++) {
            produtos[i] = produtos[i + 1];
            quantidades[i] = quantidades[i + 1];
        }
        produtos[--quantidadeItens] = null;
        quantidades[quantidadeItens] = 0;
        return true;
    }

    /** Ajusta a quantidade de um item sem ultrapassar o estoque disponível. */
    public boolean atualizarQuantidade(int codigo, int novaQuantidade) {
        int posicao = localizar(codigo);
        if (posicao == -1 || novaQuantidade <= 0
                || novaQuantidade > produtos[posicao].getEstoque()) return false;
        quantidades[posicao] = novaQuantidade;
        return true;
    }

    public boolean estaVazia() { return quantidadeItens == 0; }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < quantidadeItens; i++) {
            total += produtos[i].getPreco() * quantidades[i];
        }
        return total;
    }

    /** Valida todos os itens antes de baixar qualquer quantidade do estoque. */
    public boolean concluir() {
        if (estaVazia()) return false;
        for (int i = 0; i < quantidadeItens; i++) {
            if (produtos[i].getEstoque() < quantidades[i]) return false;
        }
        for (int i = 0; i < quantidadeItens; i++) {
            produtos[i].setEstoque(produtos[i].getEstoque() - quantidades[i]);
        }
        return true;
    }

    public String resumo() {
        StringBuilder resumo = new StringBuilder();
        for (int i = 0; i < quantidadeItens; i++) {
            Produto produto = produtos[i];
            double subtotal = produto.getPreco() * quantidades[i];
            resumo.append(String.format("- %s | Qtd: %d | Unit: R$ %.2f | Subtotal: R$ %.2f%n",
                    produto.getNome(), quantidades[i], produto.getPreco(), subtotal));
        }
        return resumo.toString();
    }

    public void exibir() {
        if (estaVazia()) {
            System.out.println("A sacola está vazia.");
            return;
        }
        System.out.print(resumo());
        System.out.printf("Total parcial: R$ %.2f%n", getTotal());
    }

    private int localizar(int codigo) {
        for (int i = 0; i < quantidadeItens; i++) {
            if (produtos[i].getCodigo() == codigo) return i;
        }
        return -1;
    }
}

