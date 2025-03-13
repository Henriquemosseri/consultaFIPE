package com.github.henriquemosseri.ConsultaFIPE.principal;

import com.github.henriquemosseri.ConsultaFIPE.model.Dados;
import com.github.henriquemosseri.ConsultaFIPE.model.Modelos;
import com.github.henriquemosseri.ConsultaFIPE.model.Veiculo;
import com.github.henriquemosseri.ConsultaFIPE.service.ConsumoAPI;
import com.github.henriquemosseri.ConsultaFIPE.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    Scanner sc = new Scanner(System.in);

    public void principal() {
        String endereco = "";
        int decisao;

        System.out.println("Bem-vindo! Escolha o número que deseja verificar:");

        while (true) {
            System.out.println("1. Carro");
            System.out.println("2. Caminhão");
            System.out.println("3. Moto");
            System.out.print("Opção: ");

            if (sc.hasNextInt()) {
                decisao = sc.nextInt();
                sc.nextLine(); // Limpa o buffer

                switch (decisao) {
                    case 1:
                        endereco = ENDERECO + "carros/marcas";
                        break;
                    case 2:
                        endereco = ENDERECO + "caminhoes/marcas";
                        break;
                    case 3:
                        endereco = ENDERECO + "motos/marcas";
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                        continue; // Volta ao início do loop
                }
                break; // Sai do loop se a escolha for válida
            } else {
                System.out.println("Entrada inválida, digite um número.");
                sc.next(); // Descarta a entrada inválida
            }
        }
        String json = consumoAPI.obterDados(endereco);
        List<Dados> marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
        System.out.println("Digite o numero que código de deseja verificar: ");
        decisao = sc.nextInt();
        sc.nextLine();

        endereco += "/" + decisao + "/modelos";
        json = consumoAPI.obterDados(endereco);
        var modelosLista = conversor.converteDados(json, Modelos.class);
        modelosLista.modelos().stream()
                .forEach(System.out::println);

        System.out.println("digite o nome do carro que deseja verificar: ");
        String carro = sc.nextLine();
        List<Dados> modelosFiltrados = modelosLista.modelos().stream()
                .filter(m -> m.nome().toUpperCase().contains(carro.toUpperCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos filtrados: ");
        modelosFiltrados.forEach(System.out::println);
        System.out.println("Digite o codigo do carro:");
        String codigo = sc.nextLine();
        endereco += "/" + codigo + "/anos";
        json = consumoAPI.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoAPI.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.converteDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("Todos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);
        sc.close();
    }
}
