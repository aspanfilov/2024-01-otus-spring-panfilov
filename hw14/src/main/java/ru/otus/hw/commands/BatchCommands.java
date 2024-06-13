package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

import static ru.otus.hw.config.JobConfig.LIBRARY_MIGRATION_JOB_NAME;

@ShellComponent
@RequiredArgsConstructor
public class BatchCommands {

    private final Job libraryMigrationJob;

    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    @SuppressWarnings("unused")
    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm")
    public void startMigrationJobWithJobLauncher() throws Exception {
        JobExecution jobExecution = jobLauncher.run(libraryMigrationJob,
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters());
        System.out.println(jobExecution);
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(LIBRARY_MIGRATION_JOB_NAME));
    }

    @ShellMethod(value = "showH2Console", key = "h2")
    public void showH2Console() throws SQLException {
        Console.main();
    }
}
