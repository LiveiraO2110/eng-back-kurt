package com.example.prototipo.service;

import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.State;
import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.repository.StateRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class SchedulerWorker {
    private final SearchTermsRepository searchTermsRepository;
    private final ProcurementRepository repository;
    private final StateRepository stateRepository;
    private final ProcurementService procurementService;

    public SchedulerWorker(SearchTermsRepository searchTermsRepository,
                           ProcurementRepository repository,
                           StateRepository stateRepository,
                           ProcurementService procurementService) {
        this.searchTermsRepository = searchTermsRepository;
        this.repository = repository;
        this.stateRepository = stateRepository;
        this.procurementService = procurementService;
    }

    @Scheduled(fixedDelay = 1800000)
    @Transactional
    public void customerSearchTerms(){
        List<SearchTerms> terms = searchTermsRepository.findAll();

        for (SearchTerms term : terms) {
            List<OpportunitiesPNCP> procurements = dailySearch(term);

            for (OpportunitiesPNCP procurement : procurements) {

                if(repository.existsByCustomer_IdAndPncpId(term.getCustomer().getId(), procurement.numero_controle_pncp())){
                    continue;
                }

                Optional<State> state = stateRepository.findByUf(procurement.uf());

                if(state.isEmpty()){
                    continue;
                }

                Procurement newProcurement = new Procurement(term.getCustomer(), procurement, state.get());

                if(procurementService.getLink(newProcurement)){
                    repository.save(newProcurement);
                }
            }
        }
    }

    private List<OpportunitiesPNCP> dailySearch(SearchTerms term){
        List<OpportunitiesPNCP> opportunities = new ArrayList<>();
        String ufs = null;

        if(!term.getStates().isEmpty()){
            StringBuilder stringBuilder = new StringBuilder();

            for (State state : term.getStates()) {
                if(stringBuilder.toString().isEmpty()){
                    stringBuilder.append(state.getUf());
                } else{
                    stringBuilder.append("|").append(state.getUf());
                }
            }

            ufs = stringBuilder.toString();
        }

        int page = 1;

        while(true){
            List<OpportunitiesPNCP> response = procurementService.searchByPage(term.getTerm(), ufs, page)
                    .stream().filter(Objects::nonNull).toList();

            if (response.isEmpty()) {
                break;
            }

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