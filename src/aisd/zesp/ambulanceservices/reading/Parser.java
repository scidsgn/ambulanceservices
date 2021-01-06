package aisd.zesp.ambulanceservices.reading;

import aisd.zesp.ambulanceservices.main.*;

public class Parser {
    public void parseHospital(State state, String[] buffer){
        int id = Integer.parseInt(buffer[0]);
        if (state.getHospitalById(id) != null ) {
            String message = "Powtarza się szpital z indeksem " + state.getHospitalById(id);
            System.out.println(message);
            System.exit(1);
        }
        Hospital hospital = new Hospital(id, buffer[1], Double.parseDouble(buffer[2]),
                Double.parseDouble(buffer[3]), Integer.parseInt(buffer[4]));
        state.addHospital(hospital);
    }

    public void parseLandmark(State state, String[] buffer){
        int id = Integer.parseInt(buffer[0]);
        if (state.getLandmarkById(id) != null ) {
            String message = "Powtarza się obiekt z indeksem " + state.getLandmarkById(id);
            System.out.println(message);
            System.exit(1);
        }
        Landmark landmark = new Landmark(id, buffer[1], Double.parseDouble(buffer[2]),
                Double.parseDouble(buffer[3]));
        state.addLandmark(landmark);
    }

    public void parseConnection(State state, String[] buffer){
        int id = Integer.parseInt(buffer[0]);
        if(state.getHospitalById(Integer.parseInt(buffer[1])) == null ||
                state.getHospitalById(Integer.parseInt(buffer[2])) == null) {
            String message = "Nie można utworzyć połączenia między nieistniejącymi szpitalami!";
            System.out.println(message);
            System.exit(1);
        }

        state.addConnection(id, state.getHospitalById(Integer.parseInt(buffer[1])),
                state.getHospitalById(Integer.parseInt(buffer[2])), Double.parseDouble(buffer[3]));
    }

    public void parsePatient(State state, String[] buffer){
        int id = Integer.parseInt(buffer[0]);
        if (state.getPatientById(id) != null ) {
            String message = "Powtarza się pacjent z indeksem " + state.getHospitalById(id);
            System.out.println(message);
            System.exit(1);
        }
        Patient patient = new Patient(id, Double.parseDouble(buffer[1]), Double.parseDouble(buffer[2]));
        state.addPatient(patient);
    }
}
