import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }

    //add methods here to meet the requirements in README.md
    public List<Volcano> eruptedInEighties() {
        return volcanos.stream().filter(item -> item.getYear() >= 1980 && item.getYear() < 1990).collect(Collectors.toList());
    }

    public String[] highVEI() {
        return volcanos.stream().filter(item -> item.getVEI() >= 6).map(item -> item.getName()).toArray(String[]::new);
    }

    public Volcano mostDeadly() {
        return volcanos.stream().filter(item -> item.getDEATHS().equals("30000")).toArray(Volcano[]::new)[0];
    }

    public double causedTsunami() {
        double total = volcanos.size();
        double causedTsunamis = volcanos.stream().filter(item -> item.getTsu().equals("tsu")).collect(Collectors.toList()).size();
        return causedTsunamis/total *100;
    }

    public String mostCommonType() {
        List<String> types = volcanos.stream().map(item -> item.getType()).distinct().collect(Collectors.toList());
        List<Integer> nums = new ArrayList<>();
        types.forEach(item -> {
            nums.add(volcanos.stream().filter(i -> i.getType().equals(item)).collect(Collectors.toList()).size());
        });
        Integer max = Collections.max(nums);
        return types.get(nums.indexOf(max));
    }

    public Integer eruptionsByCountry(String country) {
        return volcanos.stream().filter(item -> item.getCountry().equals(country)).collect(Collectors.toList()).size();
    }

    public double averageElevation() {
        double total = volcanos.size();
        double sumOfElevations = volcanos.stream().map(item -> item.getElevation()).reduce(0, Integer::sum);
        return sumOfElevations/total;
    }

    public String[] volcanoTypes() {
        return volcanos.stream().map(item -> item.getType()).distinct().toArray(String[]::new);
    }

    public double percentNorth() {
        double my = volcanos.stream().filter(item -> item.getLatitude() >= 0 && item.getLatitude() <= 90).collect(Collectors.toList()).size();
        return my/volcanos.size() * 100;
    }

}
