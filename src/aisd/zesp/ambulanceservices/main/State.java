package aisd.zesp.ambulanceservices.main;

public class State {
    private int connectionNumber = 0;

    public Hospital getHospitalById(int id) {
        Hospital hospital = null;



        return hospital;
    }

    public Patient getPatientById(int id) {
        Patient patient = null;

        return patient;
    }

    public void addPatient(Patient patient) {

    }

    public void addHospital(Hospital hospital) {

    }

    public Landmark getLandmarkById(int id) {
        Landmark landmark = null;

        return landmark;
    }

    public void addLandmark(Landmark landmark) {

    }

    public void addConnection(int id, Hospital fh, Hospital sh, double length) {
        if(connectionNumber > id) {
            String message = "Powtarza się połączenie z indeksem " + id;
            System.out.println(message);
            System.exit(1);
        }

        connectionNumber++;
    }


}
