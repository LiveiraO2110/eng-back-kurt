package com.example.prototipo.service;

import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.records.DailyResponse;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulerWorker {
    @Autowired
    private ProcurementRepository repository;
    @Autowired
    private SearchTermsRepository searchTermsRepository;

    private final RestClient restClient;

    public SchedulerWorker(){
        this.restClient = RestClient.builder()
                .baseUrl("https://pncp.gov.br")
                .build();
    }

    public void customerSearchTerms(){
        List<SearchTerms> terms = searchTermsRepository.findAll();

        for (SearchTerms term : terms) {
            List<OpportunitiesPNCP> procurements = dailySearch(term.getTerm());

            System.out.println(term.getTerm()+": ");
            for (OpportunitiesPNCP procurement : procurements) {

                Procurement newProcurement = new Procurement(term.getCustomer(), procurement);

                String[] controle =  procurement.numero_controle_pncp().replace("/", "-").split("-");

                StringBuilder builder = new StringBuilder();
                builder.append(controle[2]);

                while(true){
                    if(builder.toString().charAt(0) == '0'){
                        builder.deleteCharAt(0);
                    } else {
                        break;
                    }
                }

                int doc = 1;

                while(true) {
                    String link = String.format("/pncp-api/v1/orgaos/%s/compras/%s/%s/arquivos/%d", controle[0], controle[3], builder, doc);

                    try{
                        testLink(link);
                        doc++;
                        newProcurement.addLink("https://pncp.gov.br"+link);
                    } catch (Exception e) {
                        break;
                    }
                }

                System.out.printf(newProcurement.toString());
            }
        }
    }

    private List<OpportunitiesPNCP> dailySearch(String term){
        List<OpportunitiesPNCP> opportunities = new ArrayList<>();

        int page = 1;

        while(true){
            List<OpportunitiesPNCP> response = getAllByPage(term, page);

            List<OpportunitiesPNCP> filtered = response.stream()
                    .filter(r -> r.data_atualizacao_pncp().toLocalDate().isEqual(LocalDate.now()))
                    .toList();

            if(filtered.isEmpty()) break;

            opportunities.addAll(filtered);
            page++;
        }

        return opportunities.stream()
                .filter(op -> op.data_fim_vigencia().toLocalDate().isAfter(LocalDate.now()))
                .toList();
    }

    private List<OpportunitiesPNCP> getAllByPage(String q, int page){
        try{
            DailyResponse response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/search/")
                            .queryParam("q", q)
                            .queryParam("tipos_documento", "edital")
                            .queryParam("ordenacao", "-data")
                            .queryParam("pagina", page)
                            .queryParam("tam_pagina", 50)
                            .queryParam("status", "recebendo_proposta")
                            .queryParam("ufs", "RS")
                            .build())
                    .retrieve()
                    .body(DailyResponse.class);

            return response != null ? response.items() : List.of();
        } catch (Exception e){
            return List.of();
        }
    }

    private void testLink(String path) throws Exception {
        try{
            restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                        throw new RuntimeException("Error ao realizar a requisição");
                    }))
                    .body(String.class);
        } catch (Exception ex){
            throw new Exception("Error 404");
        }
    }
}