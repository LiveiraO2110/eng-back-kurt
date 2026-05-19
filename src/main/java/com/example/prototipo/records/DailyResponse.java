package com.example.prototipo.records;

import java.util.List;

public record DailyResponse (
        List<OpportunitiesPNCP> items,
        int total
) {
}
