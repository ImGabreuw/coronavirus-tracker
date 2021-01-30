package me.gabreuw.coronavirustracker.model.services;

import me.gabreuw.coronavirustracker.model.entities.LocationStats;
import me.gabreuw.coronavirustracker.model.factory.LocationStatsFactory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoronaVirusDataService {

    private static final String VIRUS_DATA_URL = "https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        HttpClient client = HttpClient
                .newHttpClient();

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        CSVParser records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(csvBodyReader);

        this.allStats = records.getRecords()
                .stream()
                .map(LocationStatsFactory::createFrom)
                .collect(Collectors.toList());
    }
}
