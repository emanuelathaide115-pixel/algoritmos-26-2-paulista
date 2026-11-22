# Sistema de Mini Mercado

Aplicativo em Java para terminal, sem interface gráfica, banco de dados ou bibliotecas externas. O código está dividido nas classes `Produto`, `Cliente` e `MiniMercado` e usa vetores com capacidade para até 100 registros de cada tipo.

## Funcionalidades

- Menu principal com produtos, clientes, compra, estoque e opção para sair.
- CRUD completo de produtos e clientes, com busca por código ou CPF.
- Validações para códigos, CPF, nome, e-mail, preço, quantidade e estoque.
- Compra para cliente cadastrado ou consumidor final, com vários itens, cálculo de subtotais e total.
- Verificação de disponibilidade e baixa automática do estoque depois da compra.
- Consulta e reposição de estoque pelo menu de controle.
- Relatório de estoque baixo com limite configurável e indicação de produtos sem estoque.
- Resumo geral do estoque com contagem de produtos, unidades e valor estimado a preço de venda.
- Sacola com ajuste de quantidade, remoção de itens e validação antes de confirmar a compra.
- Cupons promocionais opcionais de 5% e 10%, com desconto registrado no resumo e no total da venda.
- Histórico de vendas com data, cliente, itens e faturamento acumulado, salvo em `vendas.csv`.
- Carregamento dos CSV ao iniciar e gravação após cada alteração.

## Persistência CSV
Na pasta de execução, o sistema carrega os arquivos ao iniciar e atualiza os arquivos após mudanças:

- `produtos.csv`: `codigo;nome;preco;estoque`
- `clientes.csv`: `cpf;nome;telefone;email`
- `vendas.csv`: `data_hora;cliente;itens;total`

Os arquivos ficam na pasta de execução. `produtos.csv` é criado com produtos de exemplo se ainda não existir; `clientes.csv` e `vendas.csv` começam vazios e recebem cabeçalho no primeiro salvamento. Os arquivos são atualizados após alterações e compras. Os campos usam ponto e vírgula como separador; ponto e vírgula e quebras de linha em textos são normalizados para manter o CSV legível.

## Executar
É necessário ter o JDK 11 ou superior. No terminal, dentro da pasta que contém os arquivos Java:

```text
javac MiniMercado.java Cliente.java Produto.java Venda.java Sacola.java Promocao.java
java MiniMercado
```

