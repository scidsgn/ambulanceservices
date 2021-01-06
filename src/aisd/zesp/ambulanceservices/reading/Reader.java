package aisd.zesp.ambulanceservices.reading;

import aisd.zesp.ambulanceservices.main.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    private final Parser parser = new Parser();

    public State load(String fileName) {
        State state = new State();

        String buffer;
        int commentAmount = -1;
        int sepCount;
        int sepAllowed = 4;

        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            while ((buffer = reader.readLine()) != null) {
                sepCount = 0;
                if (buffer.trim().indexOf('#') == 0) {
                    commentAmount++;
                    if (commentAmount == 0) {
                        sepAllowed = 5;
                    }
                    else {
                        sepAllowed = 4;
                    }
                    continue;
                }
                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '|') {
                        sepCount++;
                    }
                }
                if (sepCount != sepAllowed) {
                    System.out.println("Niepoprawna ilość separatorów");
                    System.exit(1);
                }
                String[] bufferArray = buffer.split("\\s+\\|\\s+", 6);

                switch (commentAmount) {
                    case 0 -> parser.parseHospital(state, bufferArray);
                    case 1 -> parser.parseLandmark(state, bufferArray);
                    case 2 -> parser.parseConnection(state, bufferArray);
                }
            }
            reader.close();
            fileReader.close();

        } catch (IOException e) {
            System.out.println("Nie można znaleźć lub otworzyć pliku");
            System.exit(1);
        }
        return state;
    }

    public void loadPatients(State state, String fileName){
        String buffer;
        int sepCount;
        int sepAllowed = 2;

        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            while((buffer = reader.readLine()) != null) {
                sepCount = 0;
                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '|') {
                        sepCount++;
                    }
                }
                if (sepCount != sepAllowed) {
                    System.out.println("Niepoprawna ilość separatorów");
                    System.exit(1);
                }
                String[] bufferArray = buffer.split("\\s+\\|\\s+", 3);

                parser.parsePatient(state, bufferArray);
            }
        } catch (IOException e){
            System.out.println("Nie można znaleźć lub otworzyć pliku");
            System.exit(1);
        }
    }
}
