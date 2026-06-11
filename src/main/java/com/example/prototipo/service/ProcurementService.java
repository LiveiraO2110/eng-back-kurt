package com.example.prototipo.service;

import com.example.prototipo.models.Procurement;
import com.example.prototipo.records.DailyResponse;
import com.example.prototipo.records.File;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.ProcurementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class ProcurementService {
    @Autowired
    private ProcurementRepository repository;
    @Autowired
    private RestClient restClient;

    public List<Procurement> getAll(){
        return repository.findAll();
    }

    public Procurement getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Edital não encotrado"));
    }

    public List<OpportunitiesPNCP> searchByPage(String q, String ufs, int page){
        System.out.println("Fazendo requisição: "+q);

        try{
            DailyResponse response = restClient
                    .get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .path("/api/search/")
                                .queryParam("q", q)
                                .queryParam("tipos_documento", "edital")
                                .queryParam("ordenacao", "-data")
                                .queryParam("pagina", page)
                                .queryParam("tam_pagina", 50)
                                .queryParam("status", "recebendo_proposta");

                        if (ufs != null) {
                            builder.queryParam("uf", ufs);
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .body(DailyResponse.class);

            assert response != null;
            return response.items().isEmpty() ? List.of() : response.items();
        } catch (RestClientResponseException ex) {
            System.err.println("Erro na requisição: Status " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            return List.of();
        } catch (ResourceAccessException ex) {
            System.err.println("Timeout ou erro de rede ao conectar à API do PNCP: " + ex.getMessage());
            return List.of();
        } catch (Exception ex) {
            return List.of();
        }
    }

    public boolean getLink(Procurement procurement){
        String path = "/api/pncp/v1/orgaos/"+procurement.getCnpj()+"/compras/2026/"+procurement.getNumeroSequencial()+"/arquivos";

        System.out.println("Fazendo requisição: "+path);

        ParameterizedTypeReference<List<File>> typeReference =
                new ParameterizedTypeReference<List<File>>() {};

        try{
            List<File> response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("pagina", 1)
                            .queryParam("tamanhoPagina", 10)
                            .build())
                    .retrieve()
                    .body(typeReference);

            assert response != null;

            System.out.println(response);

             Optional<File> file =  response.stream()
                    .filter((f) -> f.tipoDocumentoNome().equalsIgnoreCase("edital"))
                    .findFirst();

             return file.isPresent() && isLinkPdf(file.get().url());
        } catch (RestClientResponseException ex) {
            System.err.println("Erro na requisição: Status " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            return false;
        }catch (ResourceAccessException ex) {
            System.err.println("Timeout ou erro de rede ao conectar à API do PNCP: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            return false;
        }
    }

    public boolean isLinkPdf(String url) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(10).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(25).toMillis());

        System.out.println(url);

        try {
            RestClient checkerClient = RestClient
                    .builder()
                    .baseUrl("https://pncp.gov.br")
                    .requestFactory(factory)
                    .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .defaultHeader("Accept", "application/pdf, */*")
                    .build();

            var headers = checkerClient.get()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity()
                    .getHeaders();

            MediaType contentType = headers.getContentType();

            System.out.println(contentType != null);
            System.out.println(contentType.includes(MediaType.APPLICATION_PDF));

            return contentType != null && contentType.includes(MediaType.APPLICATION_PDF);

        } catch (Exception e) {
            System.err.println("Não foi possível verificar o tipo do link: " + e.getMessage());
            return false;
        }
    }
}