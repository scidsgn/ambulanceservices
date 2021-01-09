package aisd.zesp.ambulanceservices.reading;

import aisd.zesp.ambulanceservices.main.*;

public class Parser {
    public void parseHospital(State state, String[] buffer){
        if(buffer[0].length() > 6 || buffer[2].length() > 6 || buffer[3].length() > 6 || buffer[4].length() > 6){
            throw new IllegalArgumentException("The numbers are ridiculously big!");
        }
        int id = Integer.parseInt(buffer[0]);
        if (state.getHospitalById(id) != null ) {
            throw new IllegalArgumentException("There already is hospital with ID " + state.getHospitalById(id));
        }
        Hospital hospital = new Hospital(id, buffer[1], Double.parseDouble(buffer[2]),
                Double.parseDouble(buffer[3]), Integer.parseInt(buffer[5]));
        state.addHospital(hospital);
    }

    public void parseLandmark(State state, String[] buffer){
        if(buffer[0].length() > 6 || buffer[2].length() > 6 || buffer[3].length() > 6){
            throw new IllegalArgumentException("The numbers are ridiculously big!");
        }
        int id = Integer.parseInt(buffer[0]);
        if (state.getLandmarkById(id) != null ) {
            throw new IllegalArgumentException("There already is landmark with ID " + state.getLandmarkById(id));
        }
        Landmark landmark = new Landmark(id, buffer[1], Double.parseDouble(buffer[2]),
                Double.parseDouble(buffer[3]));
        state.addLandmark(landmark);
    }

    public void parseConnection(State state, String[] buffer){
        int id = Integer.parseInt(buffer[0]);
        if(state.getHospitalById(Integer.parseInt(buffer[1])) == null ||
                state.getHospitalById(Integer.parseInt(buffer[2])) == null) {
            throw new IllegalArgumentException("Connections can only be established between existing hospitals.");
        }

        state.addConnection(id, state.getHospitalById(Integer.parseInt(buffer[1])),
                state.getHospitalById(Integer.parseInt(buffer[2])), Double.parseDouble(buffer[3]));
    }

    public void parsePatient(State state, String[] buffer){
        if(buffer[0].length() > 6 || buffer[1].length() > 6 || buffer[2].length() > 6){
            throw new IllegalArgumentException("The numbers are ridiculously big!");
        }
        int id = Integer.parseInt(buffer[0]);
        if (state.getPatientById(id) != null ) {
            throw new IllegalArgumentException("There already is patient with ID " + state.getPatientById(id));
        }
        Patient patient = new Patient(id, Double.parseDouble(buffer[1]), Double.parseDouble(buffer[2]));
        state.addPatient(patient);
    }
}
