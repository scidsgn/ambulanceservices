package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.graph.Graph;
import aisd.zesp.ambulanceservices.main.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class MapCanvas extends Canvas {
    private State state = null;

    public MapCanvas(int width, int height) {
        super(width, height);
    }

    public void setState(State state) {
        this.state = state;
        draw();
    }

    private Point worldToCanvas(Point point) {
        return new Point(100 + point.getX() * 2, 100 + point.getY() * 2);
    }

    private void drawConvexHull(GraphicsContext g) {
        List<Point> points = state.getConvexHull().getHullPoints();

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

        g.setStroke(Color.DARKGREY);
        g.setLineDashes(10, 10);
        g.stroke();
    }

    private void drawPaths(GraphicsContext g) {
        Graph<Point> graph = state.getConnectionsGraph();
        List<Point> graphNodes = graph.getNodes();

        g.beginPath();
        for (int i = 0; i < graphNodes.size(); i++) {
            for (int j = i; j < graphNodes.size(); j++) {
                Point p1 = graphNodes.get(i);
                Point p2 = graphNodes.get(j);
                Double length = graph.getLength(p1, p2);

                if (length != null) {
                    Point screenPoint1 = worldToCanvas(p1);
                    Point screenPoint2 = worldToCanvas(p2);

                    g.moveTo(screenPoint1.getX(), screenPoint1.getY());
                    g.lineTo(screenPoint2.getX(), screenPoint2.getY());
                }
            }
        }

        g.setStroke(Color.LIGHTGRAY);
        g.setLineDashes(1);
        g.setLineWidth(2);
        g.stroke();
    }

    private void drawHospitals(GraphicsContext g) {
        for (Hospital h : state.getHospitalList()) {
            Point screenPoint = worldToCanvas(h);

            g.setFill(Color.BLUE);
            g.fillRect(screenPoint.getX() - 5, screenPoint.getY() - 5, 11, 11);
        }
    }

    private void drawLandmarks(GraphicsContext g) {
        for (Landmark l : state.getLandmarkList()) {
            Point screenPoint = worldToCanvas(l);

            g.setFill(Color.DARKGREY);
            g.fillRect(screenPoint.getX(), screenPoint.getY(), 1, 1);
        }
    }

    private void drawPatient(GraphicsContext g, Patient patient) {
        PatientState patientState = patient.getPatientState();

        if (patientState == PatientState.WAITING) {
            Point screenPoint = worldToCanvas(patient);

            g.setFill(Color.WHITE);
            g.fillRect(screenPoint.getX() - 3, screenPoint.getY() - 3, 7, 7);
        }
    }

    private void drawPatients(GraphicsContext g) {
        for (Patient p : state.getPatientList()) {
            drawPatient(g, p);
        }
    }

    public void draw() {
        GraphicsContext g = getGraphicsContext2D();

        g.setFill(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (state != null) {
            drawConvexHull(g);
            drawPaths(g);
            drawHospitals(g);
            drawLandmarks(g);
            drawPatients(g);
        }
    }
}
