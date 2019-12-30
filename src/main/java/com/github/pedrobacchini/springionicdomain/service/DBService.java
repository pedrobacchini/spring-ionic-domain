package com.github.pedrobacchini.springionicdomain.service;

import com.github.pedrobacchini.springionicdomain.domain.*;
import com.github.pedrobacchini.springionicdomain.enums.EstadoPagamento;
import com.github.pedrobacchini.springionicdomain.enums.Perfil;
import com.github.pedrobacchini.springionicdomain.enums.TipoCliente;
import com.github.pedrobacchini.springionicdomain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DBService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void instantiateTestDatabase() throws ParseException {

        Categoria categoria1 = new Categoria(null, "Informática");
        Categoria categoria2 = new Categoria(null, "Escrítorio");
        Categoria categoria3 = new Categoria(null, "Cama, mesa e banho");
        Categoria categoria4 = new Categoria(null, "Eletrônicos");
        Categoria categoria5 = new Categoria(null, "Jardinagem");
        Categoria categoria6 = new Categoria(null, "Decoração");
        Categoria categoria7 = new Categoria(null, "Perfumaria");

        Produto produto1 = new Produto(null, "Computador", 2000D);
        Produto produto2 = new Produto(null, "Impressora", 800D);
        Produto produto3 = new Produto(null, "Mouse", 80D);
        Produto produto4 = new Produto(null, "Mesa de escritorio", 300D);
        Produto produto5 = new Produto(null, "Toalha", 50D);
        Produto produto6 = new Produto(null, "Colcha", 200D);
        Produto produto7 = new Produto(null, "TV true color", 1200D);
        Produto produto8 = new Produto(null, "Roçadeira", 800D);
        Produto produto9 = new Produto(null, "Abajour", 100D);
        Produto produto10 = new Produto(null, "Pendente", 180D);
        Produto produto11 = new Produto(null, "Shampoo", 90D);

        categoria1.addAllProduto(Arrays.asList(produto1, produto2, produto3));
        categoria2.addAllProduto(Arrays.asList(produto2, produto4));
        categoria3.addAllProduto(Arrays.asList(produto5, produto6));
        categoria4.addAllProduto(Arrays.asList(produto1, produto2, produto3, produto7));
        categoria5.addProduto(produto8);
        categoria6.addAllProduto(Arrays.asList(produto9, produto10));
        categoria7.addProduto(produto11);

        categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2, categoria3, categoria4,
                categoria5, categoria6, categoria7));

        produtoRepository.saveAll(Arrays.asList(produto1, produto2, produto3, produto4, produto5,
                produto6, produto7, produto8, produto9, produto10, produto11));

        Estado estado1 = new Estado(null, "Minas Gerais");
        Estado estado2 = new Estado(null, "São Paulo");

        Cidade cidade1 = new Cidade(null, "Uberlândia", estado1);
        Cidade cidade2 = new Cidade(null, "São Paulo", estado2);
        Cidade cidade3 = new Cidade(null, "Campinas", estado2);

        estado1.getCidades().add(cidade1);
        estado2.getCidades().addAll(Arrays.asList(cidade2, cidade3));

        estadoRepository.saveAll(Arrays.asList(estado1, estado2));
        cidadeRepository.saveAll(Arrays.asList(cidade1, cidade2, cidade3));

        Cliente mariaSilva = new Cliente(null,
                "Maria Silva",
                "pedroheinrique@gmail.com",
                "36378912377",
                TipoCliente.PESSOAFISICA,
                bCryptPasswordEncoder.encode("123"));
        mariaSilva.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

        Cliente anaCosta = new Cliente(null,
                "Ana Costa",
                "pedrobacchini@outlook.com",
                "68813512244",
                TipoCliente.PESSOAFISICA,
                bCryptPasswordEncoder.encode("123"));
        anaCosta.getTelefones().add("66996193389");
        anaCosta.addPerfil(Perfil.ADMIN);

        Endereco endereco1 = new Endereco(null, "Rua Flores",
                "300", "Apto 203",
                "Jardim", "38220834", mariaSilva, cidade1);
        Endereco endereco2 = new Endereco(null, "Avenida Matos",
                "105", "Sala 800",
                "Centro", "38777012", mariaSilva, cidade2);
        Endereco endereco3 = new Endereco(null, "Avenida Floriano",
                "2106", null,
                "Centro", "5646546", anaCosta, cidade2);

        mariaSilva.getEnderecos().addAll(Arrays.asList(endereco1, endereco2));
        anaCosta.getEnderecos().add(endereco3);

        clienteRepository.saveAll(Arrays.asList(mariaSilva, anaCosta));
        enderecoRepository.saveAll(Arrays.asList(endereco1, endereco2, endereco3));

        SimpleDateFormat formatDiaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat formatDia = new SimpleDateFormat("dd-MM-yyyy");
        Date instante1, instante2, dataVencimento;

        instante1 = formatDiaHora.parse("30-09-0217 10:32");
        instante2 = formatDiaHora.parse("10-10-2017 19:35");
        dataVencimento = formatDia.parse("20-10-2017");

        Pedido pedido1 = new Pedido(instante1, endereco1, mariaSilva);
        Pagamento pagamentoComCartao = new PagamentoComCartao(null, EstadoPagamento.QUITADO, pedido1, 6);
        pedido1.setPagamento(pagamentoComCartao);

        Pedido pedido2 = new Pedido(instante2, endereco2, mariaSilva);
        Pagamento pagamentoComBoleto = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, pedido2, dataVencimento, null);
        pedido2.setPagamento(pagamentoComBoleto);

        pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2));
        pagamentoRepository.saveAll(Arrays.asList(pagamentoComBoleto, pagamentoComCartao));

//        cliente1.getPedidos().addAll(Arrays.asList(pedido1, pedido2));
//        clienteRepository.save(cliente1);

        ItemPedido itemPedido1 = new ItemPedido(pedido1, produto1, 0D, 1, 2000D);
        ItemPedido itemPedido2 = new ItemPedido(pedido1, produto3, 0D, 2, 80D);
        pedido1.getItens().addAll(Arrays.asList(itemPedido1, itemPedido2));

        ItemPedido itemPedido3 = new ItemPedido(pedido2, produto2, 100D, 1, 800D);
        pedido2.getItens().add(itemPedido3);

        produto1.getItens().add(itemPedido1);
        produto2.getItens().add(itemPedido3);
        produto3.getItens().add(itemPedido2);

        itemPedidoRepository.saveAll(Arrays.asList(itemPedido1, itemPedido2, itemPedido3));
    }
}
