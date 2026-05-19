package com.example.prototipo.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpportunitiesPNCP(
        String title,
        String description,
        String numero_controle_pncp,
        String orgao_cnpj,
        String orgao_nome,
        String municipio_nome,
        String uf,
        String modalidade_licitacao_nome,
        LocalDateTime data_atualizacao_pncp,
        LocalDateTime data_inicio_vigencia,
        LocalDateTime data_fim_vigencia
) {
}