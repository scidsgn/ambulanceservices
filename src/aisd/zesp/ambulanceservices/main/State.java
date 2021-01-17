package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.graham.ConvexHull;
import aisd.zesp.ambulanceservices.graham.GrahamAlgorithm;
import aisd.zesp.ambulanceservices.graph.DijkstraAlgorithm;
import aisd.zesp.ambulanceservices.graph.Graph;
import aisd.zesp.ambulanceservices.graph.GraphConstructorAlgorithm;

import java.util.*;

public class State {
    private final List<Hospital> hospitalList;
    private final List<Patient> patientList;
    private final List<Landmark> landmarkList;

    private final Set<Integer> connectionIds;
    private final GraphConstructorAlgorithm graphConstructor;
    private Graph<Point> connectionsGraph;
    private final List<List<List<Point>>> nextHospitalPaths;
    private ConvexHull convexHull;

    public State() {
        hospitalList = new ArrayList<>();
        patientList = new ArrayList<>();
        landmarkList = new ArrayList<>();

        graphConstructor = new GraphConstructorAlgorithm();
        connectionIds = new HashSet<>();

        nextHospitalPaths = new ArrayList<>();
    }

    public ConvexHull getConvexHull() {
        return convexHull;
    }

    public Graph<Point> getConnectionsGraph() {
        return connectionsGraph;
    }

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public List<Landmark> getLandmarkList() {
        return landmarkList;
    }

    public Hospital getHospitalById(int id) {
        for (Hospital hospital : hospitalList) {
            if (hospital.getId() == id) {
                return hospital;
            }
        }

        return null;
    }

    public Patient getPatientById(int id) {
        for (Patient patient : patientList) {
            if (patient.getId() == id) {
                return patient;
            }
        }

        return null;
    }

    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new NullPointerException("Patient cannot be null.");
        }
        if (getPatientById(patient.getId()) != null) {
            throw new IllegalArgumentException("Patient with that ID already added.");
        }
        patientList.add(patient);
    }

    public void addHospital(Hospital hospital) throws IllegalArgumentException, NullPointerException {
        if (hospital == null) {
            throw new NullPointerException("Hospital cannot be null.");
        }
        if (getHospitalById(hospital.getId()) != null) {
            throw new IllegalArgumentException("Hospital with that ID already added.");
        }

        hospitalList.add(hospital);
    }

    public Landmark getLandmarkById(int id) {
        for (Landmark landmark : landmarkList) {
            if (landmark.getId() == id) {
                return landmark;
            }
        }

        return null;
    }

    public void addLandmark(Landmark landmark) throws IllegalArgumentException, NullPointerException {
        if (landmark == null) {
            throw new NullPointerException("Landmark cannot be null.");
        }
        if (getLandmarkById(landmark.getId()) != null) {
            throw new IllegalArgumentException("Landmark with that ID already added.");
        }

        landmarkList.add(landmark);
    }

    public void addConnection(int id, Hospital fh, Hospital sh, double length) {
        if (connectionIds.contains(id)) {
            throw new IllegalArgumentException("Connection with that ID already added.");
        }
        if (fh == null || sh == null) {
            throw new NullPointerException("Hospital cannot be null.");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than zero.");
        }

        connectionIds.add(id);
        graphConstructor.addLine(fh, sh, length);
    }

    public void finalizeConnections() {
        connectionsGraph = graphConstructor.constructGraph();

        DijkstraAlgorithm<Point> algo = new DijkstraAlgorithm<>(connectionsGraph);

        for (int i = 0; i < hospitalList.size(); i++) {
            List<List<Point>> paths = new ArrayList<>();
            Hospital startHospital = hospitalList.get(i);

            algo.execute(startHospital);

            for (int j = 0; j < hospitalList.size(); j++) {
                if (j == i) {
                    continue;
                }
                Hospital targetHospital = hospitalList.get(j);
                List<Point> path = algo.getPath(targetHospital);

                if (path != null) {
                    paths.add(path);
                }
            }

            paths.sort(Comparator.comparingDouble(p -> connectionsGraph.getPathLength(p)));
            nextHospitalPaths.add(paths);
        }
    }

    public Hospital getNextHospital(Hospital hospital, List<Hospital> excluded) throws IllegalArgumentException {
        int index = hospitalList.indexOf(hospital);
        if (index == -1) {
            throw new IllegalArgumentException("Hospital not present in the list.");
        }

        for (List<Point> path : nextHospitalPaths.get(index)) {
            Hospital target = (Hospital) path.get(0);

            if (!excluded.contains(target)) {
                return target;
            }
        }

        return null;
    }

    public Patient getNextPatient() {
        for (Patient patient : patientList) {
            if (patient.getPatientState() == PatientState.WAITING) {
                return patient;
            }
        }
        return null;
    }

    public void finalizeConvexHull() {
        GrahamAlgorithm algo = new GrahamAlgorithm();
        List<Point> points = new ArrayList<>();

        points.addAll(landmarkList);
        points.addAll(hospitalList);

        convexHull = algo.createConvexHull(points);
    }

    public Hospital findNearestHospital(Point point) {
        double minDist = Double.POSITIVE_INFINITY;
        Hospital minHospital = null;

        for (Hospital hospital : hospitalList) {
            double distance = point.getDistance(hospital);

            if (distance < minDist) {
                minDist = distance;
                minHospital = hospital;
            }
        }

        return minHospital;
    }

    private int generateNewPatientID() {
        int id = patientList.size() + 1;

        while (getPatientById(id) != null) {
            id += 1;
        }

        return id;
    }

    public void addPatientFromCanvas(double x, double y) {
        Patient patient = new Patient(generateNewPatientID(), x, y);
        patientList.add(patient);
    }
}
