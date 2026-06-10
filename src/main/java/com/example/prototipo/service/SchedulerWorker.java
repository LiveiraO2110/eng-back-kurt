package com.example.prototipo.service;

import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.State;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulerWorker {
    @Autowired
    private ProcurementRepository repository;
    @Autowired
    private SearchTermsRepository searchTermsRepository;
    @Autowired
    private StateRepository stateRepository;

    @Scheduled(fixedDelay = 1800000)
    public void customerSearchTerms(){
        List<SearchTerms> terms = searchTermsRepository.findAll();

        for (SearchTerms term : terms) {
            List<OpportunitiesPNCP> procurements = dailySearch(term.getTerm());

            System.out.println(term.getTerm()+": ");
            for (OpportunitiesPNCP procurement : procurements) {

                if(repository.existsByCustomer_IdAndPncpId(term.getCustomer().getId(), procurement.numero_controle_pncp())){
                    continue;
                }

                State state = stateRepository.findByUf(procurement.uf())
                        .orElseThrow(() -> new EntityNotFoundException("Estado com UF: "+procurement.uf()+" não encontrado"));

                Procurement newProcurement = new Procurement(term.getCustomer(), procurement, state);

                ProcurementService.getLink(newProcurement);

                repository.save(newProcurement);
            }
        }
    }

    private List<OpportunitiesPNCP> dailySearch(String term){
        List<OpportunitiesPNCP> opportunities = new ArrayList<>();

        int page = 1;

        while(true){
            List<OpportunitiesPNCP> response = ProcurementService.searchByPage(term, "RS|SC", page);

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
}