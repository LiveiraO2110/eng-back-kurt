package com.example.prototipo.service;

import com.example.prototipo.beans.DateMap;
import com.example.prototipo.enums.Date;
import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.records.DailyResponse;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.records.requests.ProcurementRequest;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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

    public List<Procurement> getAllByCustomer(Long customerId){
        return repository.findByCustomer_Id(customerId);
    }

    public Procurement createProcurement (ProcurementRequest request){
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente com id "+request.customerId()+" não encontrado"));

        return repository.save(new Procurement(customer, request.procurement()));
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

    @Transactional
    public static void getLinks(Procurement procurement){
        String[] controle =  procurement.getPncpId().replace("/", "-").split("-");

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
                ProcurementService.testLink(link);
                doc++;
                procurement.addLink("https://pncp.gov.br"+link);
            procurement.setEditalLink(String.format("https://pncp.gov.br/app/editais/%s/%s/%s", controle[0], controle[3], builder));
            } catch (Exception e) {
                break;
            }
        }
    }

    private static void testLink(String path) throws Exception {
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