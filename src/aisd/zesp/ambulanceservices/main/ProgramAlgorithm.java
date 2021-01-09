package aisd.zesp.ambulanceservices.main;

public class ProgramAlgorithm {
    private State state = null;
    private Patient currentPatient;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
