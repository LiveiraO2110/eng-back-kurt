package com.example.prototipo;

import com.example.prototipo.service.SchedulerWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BackEndRunner implements CommandLineRunner {
    @Autowired
    private SchedulerWorker worker;

    @Override
    public void run(String... args) throws Exception {
    }
}