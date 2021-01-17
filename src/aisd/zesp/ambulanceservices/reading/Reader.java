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
        int lineNumber = 0;
        int commentAmount = -1;
        int sepCount;
        int sepAllowed = 4;

        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            while ((buffer = reader.readLine()) != null) {
                lineNumber++;
                sepCount = 0;

                if (buffer.trim().indexOf('#') == 0) {
                    commentAmount++;
                    if (commentAmount == 0) {
                        sepAllowed = 5;
                    }
                    else {
                        sepAllowed = 3;
                    }
                    continue;
                }

                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '|') {
                        sepCount++;
                    }
                }
                if (sepCount != sepAllowed) {
                    throw new IllegalArgumentException("Wrong number of separators at line " + lineNumber);
                }

                String[] bufferArray = buffer.split("\\s+\\|\\s+", 6);

                if(commentAmount == 0){
                    parser.parseHospital(state, bufferArray);
                }
                else if(commentAmount == 1){
                    parser.parseLandmark(state, bufferArray);
                }
                else if(commentAmount == 2){
                    parser.parseConnection(state, bufferArray);
                }
            }

            reader.close();
            fileReader.close();
        } catch (IOException e) {
           throw new IllegalArgumentException("File has to be accessible!");
        }

        return state;
    }

    public void loadPatients(State state, String fileName){
        if  (state == null) {
            throw new NullPointerException("State cannot be null.");
        }

        String buffer;
        int sepCount;
        int sepAllowed = 2;

        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            while((buffer = reader.readLine()) != null) {
                sepCount = 0;

                if (buffer.trim().indexOf('#') == 0) {
                    continue;
                }
                
                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '|') {
                        sepCount++;
                    }
                }
                if (sepCount != sepAllowed) {
                    throw new IllegalArgumentException("Too many separators in file");
                }

                String[] bufferArray = buffer.split("\\s+\\|\\s+", 3);
                parser.parsePatient(state, bufferArray);
            }
        } catch (IOException e){
            throw new IllegalArgumentException("File has to be accessible!");
        }
    }
}
