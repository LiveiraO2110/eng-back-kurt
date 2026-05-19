package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.records.DailyResponse;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                .baseUrl("https://pncp.gov.br/api/search/")
                .build();
    }

    public void customerSearchTerms(){
        List<OpportunitiesPNCP> procurements = dailySearch();
        List<SearchTerms> terms = searchTermsRepository.findAll();

        for (SearchTerms term : terms) {
            for (OpportunitiesPNCP procurement : procurements) {
                if(
                        procurement.description() != null &&
                        procurement.description().toLowerCase().contains(term.getTerm().toLowerCase())
                ){
//                    boolean alreadyExists = repository.existsByCustomer_IdAndPncpId(term.getCustomer().getId(), procurement.numero_controle_pncp());
//
//                    if (!alreadyExists) {
//                        Procurement newProcurement = new Procurement(term.getCustomer(), procurement);
//                        repository.save(newProcurement);
//                    }

                    System.out.println(procurement);
                }
            }
        }
    }

    public List<OpportunitiesPNCP> dailySearch(){
        List<OpportunitiesPNCP> opportunities = new ArrayList<>();

        int page = 1;

        while(true){
            List<OpportunitiesPNCP> response = getAllByPage(page);

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

    private List<OpportunitiesPNCP> getAllByPage(int page){
        try{
            DailyResponse response = restClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
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
}