package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.graph.Graph;
import aisd.zesp.ambulanceservices.main.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class MapCanvas extends Canvas {
    private final ProgramAlgorithm programAlgorithm;

    private final Color convexHullFill = Color.color(12/255., 11/255., 20/255.);
    private final Color convexHullStroke = Color.color(39/255., 46/255., 59/255.);
    private final Color graphEdgeStroke = Color.color(127/255., 127/255., 127/255.);
    private final Color landmarkTextFill = Color.color(0/255., 133/255., 245/255.);

    public MapCanvas(int width, int height, ProgramAlgorithm programAlgorithm) {
        super(width, height);
        this.programAlgorithm = programAlgorithm;
    }

    private Point worldToCanvas(Point point) {
        return new Point(100 + point.getX() * 2, 100 + point.getY() * 2);
    }

    private void drawConvexHull(GraphicsContext g) {
        List<Point> points = programAlgorithm.getState().getConvexHull().getHullPoints();

        g.beginPath();
        for (Point p : points) {
            Point screenPoint = worldToCanvas(p);
            if (p == points.get(0)) {
                g.moveTo(screenPoint.getX(), screenPoint.getY());
            } else {
                g.lineTo(screenPoint.getX(), screenPoint.getY());
            }
        }
        g.closePath();

        g.setFill(convexHullFill);
        g.fill();

        g.setStroke(convexHullStroke);
        g.setLineWidth(2);
        g.stroke();
    }

    private void drawPaths(GraphicsContext g) {
        Graph<Point> graph = programAlgorithm.getState().getConnectionsGraph();
        List<Point> graphNodes = graph.getNodes();

        g.beginPath();
        g.setFill(Color.DARKGREY);
        for (int i = 0; i < graphNodes.size(); i++) {
            for (int j = i + 1; j < graphNodes.size(); j++) {
                Point p1 = graphNodes.get(i);
                Point p2 = graphNodes.get(j);
                Double length = graph.getLength(p1, p2);

                if (length != null) {
                    Point screenPoint1 = worldToCanvas(p1);
                    Point screenPoint2 = worldToCanvas(p2);
                    Point screenPathMiddle = new Point(0.5, screenPoint1, screenPoint2);

                    g.moveTo(screenPoint1.getX(), screenPoint1.getY());
                    g.lineTo(screenPoint2.getX(), screenPoint2.getY());

                    g.fillText("L: " + length, screenPathMiddle.getX() + 5, screenPathMiddle.getY() + 5);
                }
            }
        }

        g.setStroke(graphEdgeStroke);
        g.setLineWidth(4);
        g.stroke();
    }

    private void drawHospitals(GraphicsContext g) {
        for (Hospital h : programAlgorithm.getState().getHospitalList()) {
            Point screenPoint = worldToCanvas(h);

            g.setFill(Color.BLACK);
            g.fillRect(screenPoint.getX() - 9, screenPoint.getY() - 9, 18, 18);
            g.drawImage(AppIcons.hospital, screenPoint.getX() - 8, screenPoint.getY() - 8);

            g.setFill(Color.WHITE);
            g.fillText("S" + h.getId(), screenPoint.getX() + 16, screenPoint.getY() + 4);
        }
    }

    private void drawLandmarks(GraphicsContext g) {
        for (Landmark l : programAlgorithm.getState().getLandmarkList()) {
            Point screenPoint = worldToCanvas(l);

            g.setFill(Color.BLACK);
            g.fillRect(screenPoint.getX() - 9, screenPoint.getY() - 9, 18, 18);
            g.drawImage(AppIcons.monument, screenPoint.getX() - 8, screenPoint.getY() - 8);

            g.setFill(landmarkTextFill);
            g.fillText("X" + l.getId(), screenPoint.getX() + 16, screenPoint.getY() + 4);
        }
    }

    private void drawPatient(GraphicsContext g, Patient patient) {
        PatientState patientState = patient.getPatientState();
        Point screenPoint = worldToCanvas(patient);

        Image img = AppIcons.patientWaiting;

        if (patientState == PatientState.ABANDONED || patientState == PatientState.ACCEPTED) {
            return;
        } else if (patientState == PatientState.OUTOFBOUNDS) {
            img = AppIcons.patientAbandoned;
            g.setFill(Color.RED);
        } else if (patientState == PatientState.REJECTED) {
            screenPoint = worldToCanvas(programAlgorithm.getCurrentHospital());
            screenPoint.setY(screenPoint.getY() + 20);

            g.setFill(Color.RED);
        } else if (patient == programAlgorithm.getCurrentPatient()) {
            if (patientState != PatientState.WAITING) {
                screenPoint = worldToCanvas(programAlgorithm.getCurrentHospital());
                screenPoint.setY(screenPoint.getY() + 20);
            }

            g.setFill(Color.YELLOW);
        } else {
            g.setFill(Color.WHITE);
        }

        g.setFill(Color.BLACK);
        g.fillRect(screenPoint.getX() - 9, screenPoint.getY() - 9, 18, 18);
        g.drawImage(img, screenPoint.getX() - 8, screenPoint.getY() - 8);

        g.setFill(Color.WHITE);
        g.fillText("P" + patient.getId(), screenPoint.getX() + 16, screenPoint.getY() + 4);
    }

    private void drawPatients(GraphicsContext g) {
        for (Patient p : programAlgorithm.getState().getPatientList()) {
            drawPatient(g, p);
        }
    }

    public void draw() {
        GraphicsContext g = getGraphicsContext2D();

        g.setFill(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (programAlgorithm.getState() != null) {
            drawConvexHull(g);
            drawPaths(g);
            drawHospitals(g);
            drawLandmarks(g);
            drawPatients(g);
        }
    }
}
