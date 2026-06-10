package com.example.prototipo.service;

import com.example.prototipo.beans.DateMap;
import com.example.prototipo.enums.Date;
import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.records.DailyResponse;
import com.example.prototipo.records.FileResponse;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProcurementService {
    @Autowired
    private ProcurementRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private final static RestClient restClient = RestClient
            .builder()
            .baseUrl("https://pncp.gov.br")
            .build();
    @Autowired
    private DateMap dateMap;

    public List<Procurement> getAll(){
        return repository.findAll();
    }

    public Procurement getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Edital não encotrado"));
    }

    public List<Procurement> search(Long customerId, Date date, String ufs){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com id "+customerId+" não encontrado"));

        LocalDate target = LocalDate.now().minusDays(dateMap.getDayValue(date));
        List<OpportunitiesPNCP> opportunities = new ArrayList<>();

        for(SearchTerms term : customer.getSearchTerms()){
            int page = 1;

            while(true){
                List<OpportunitiesPNCP> opps = searchByPage(term.getTerm(), ufs, page)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(op -> op.data_atualizacao_pncp() != null && !op.data_atualizacao_pncp().isAfter(target.atStartOfDay()))
                        .toList();

                if(opps.isEmpty()){
                    break;
                }

                opportunities.addAll(opps);

                page++;
            }
        }

        return null;
    }

    public static List<OpportunitiesPNCP> searchByPage(String q, String ufs, int page){
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
                            .queryParam("ufs", ufs)
                            .build())
                    .retrieve()
                    .body(DailyResponse.class);

            assert response != null;
            return response.items().isEmpty() ? List.of() : response.items();
        } catch (Exception ex){
            return List.of();
        }
    }

    public static void getLink(Procurement procurement){
        String path = "/pncp-api/v1/orgaos/";

        FileResponse response = restClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .retrieve()
                .body(FileResponse.class);
    }
}