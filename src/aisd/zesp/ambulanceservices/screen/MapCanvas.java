package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.graph.Graph;
import aisd.zesp.ambulanceservices.main.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;

public class MapCanvas extends Canvas {
    private final ProgramAlgorithm programAlgorithm;

    private final Color convexHullFill = Color.color(12/255., 11/255., 20/255.);
    private final Color convexHullStroke = Color.color(39/255., 46/255., 59/255.);
    private final Color graphEdgeStroke = Color.color(127/255., 127/255., 127/255.);
    private final Color landmarkTextFill = Color.color(0/255., 133/255., 245/255.);

    private final Point viewOffset = new Point(0, 0);
    private double viewScale = 1.0;
    private Point lastScenePoint;

    public MapCanvas(int width, int height, ProgramAlgorithm programAlgorithm) {
        super(width, height);
        this.programAlgorithm = programAlgorithm;

        setupEvents();
    }
    
    private void setupEvents() {
        this.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                lastScenePoint = new Point(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            }
        });
        this.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                Point point = new Point(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                translate(point.getX() - lastScenePoint.getX(), point.getY() - lastScenePoint.getY());

                lastScenePoint = point;
            }
        });
        this.setOnScroll(scrollEvent -> {
            Point canvasPoint = new Point(scrollEvent.getSceneX(), scrollEvent.getSceneY() - getLayoutY());

            scale(Math.pow(1.2, Math.signum(scrollEvent.getDeltaY())), canvasToWorld(canvasPoint));
        });
    }

    private void scale(double factor, Point scaleOrigin) {
        double newScale = viewScale * factor;

        viewOffset.setX(viewOffset.getX() + viewScale * scaleOrigin.getX() - newScale * scaleOrigin.getX());
        viewOffset.setY(viewOffset.getY() + viewScale * scaleOrigin.getY() - newScale * scaleOrigin.getY());

        viewScale = newScale;

        draw();
    }

    private void translate(double dX, double dY) {
        viewOffset.setX(viewOffset.getX() + dX);
        viewOffset.setY(viewOffset.getY() + dY);

        draw();
    }

    public void autoAlignView() {
        double[] bbox = programAlgorithm.getState().getConvexHull().getBoundingBox();
        double w = bbox[2];
        double h = bbox[3];

        double mapRatio = getWidth() / getHeight();
        double hullRatio = w / h;

        double scale;

        if (mapRatio > hullRatio) {
            scale = (getHeight() - 50) / h;
        } else {
            scale = (getWidth() - 50) / w;
        }

        scale(scale, new Point(0, 0));
        translate((getWidth() - w * scale) / 2, (getHeight() - h * scale) / 2);
    }

    public Point worldToCanvas(Point point) {
        return new Point(
                point.getX() * viewScale + viewOffset.getX(),
                point.getY() * viewScale + viewOffset.getY()
        );
    }

    public Point canvasToWorld(Point point) {
        return new Point(
                (point.getX() - viewOffset.getX()) / viewScale,
                (point.getY() - viewOffset.getY()) / viewScale
        );
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

    private double[] measureText(String str) {
        Text text = new Text(str);
        double[] dims = new double[2];

        dims[0] = text.getLayoutBounds().getWidth();
        dims[1] = text.getLayoutBounds().getHeight();

        return dims;
    }

    private void drawLength(GraphicsContext g, Point p1, Point p2, double length) {
        Point middle = new Point(0.5, p1, p2);
        String lengthText = String.valueOf(Math.round(length));

        double[] textBounds = measureText(lengthText);

        g.setFill(Color.ORANGE);
        g.fillRect(
                middle.getX() - textBounds[0] / 2 - 8, middle.getY() - textBounds[1] / 2,
                textBounds[0] + 16, textBounds[1]
        );
        g.setFill(Color.BLACK);
        g.fillText(lengthText, middle.getX() - textBounds[0] / 2, middle.getY() + 4);
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

                    g.moveTo(screenPoint1.getX(), screenPoint1.getY());
                    g.lineTo(screenPoint2.getX(), screenPoint2.getY());
                }
            }
        }
        g.setStroke(graphEdgeStroke);
        g.setLineWidth(4);
        g.stroke();

        for (int i = 0; i < graphNodes.size(); i++) {
            for (int j = i + 1; j < graphNodes.size(); j++) {
                Point p1 = graphNodes.get(i);
                Point p2 = graphNodes.get(j);
                Double length = graph.getLength(p1, p2);

                if (length != null) {
                    Point screenPoint1 = worldToCanvas(p1);
                    Point screenPoint2 = worldToCanvas(p2);

                    g.setFill(Color.BLACK);
                    g.fillRect(screenPoint1.getX() - 3, screenPoint1.getY() - 3, 7, 7);
                    g.setFill(Color.WHITE);
                    g.fillRect(screenPoint1.getX() - 2, screenPoint1.getY() - 2, 5, 5);

                    drawLength(g, screenPoint1, screenPoint2, length);
                }
            }
        }
    }

    private void drawHospitals(GraphicsContext g) {
        for (Hospital h : programAlgorithm.getState().getHospitalList()) {
            Point screenPoint = worldToCanvas(h);

            g.setFill(Color.BLACK);
            g.fillRect(screenPoint.getX() - 9, screenPoint.getY() - 9, 18, 18);
            g.drawImage(
                    h.getVacantBeds() == 0 ? AppAssets.hospitalFull : AppAssets.hospital,
                    screenPoint.getX() - 8, screenPoint.getY() - 8
            );

            g.setFill(Color.WHITE);
            g.fillText("S" + h.getId(), screenPoint.getX() + 16, screenPoint.getY() + 4);
        }
    }

    private void drawLandmarks(GraphicsContext g) {
        for (Landmark l : programAlgorithm.getState().getLandmarkList()) {
            Point screenPoint = worldToCanvas(l);

            g.setFill(Color.BLACK);
            g.fillRect(screenPoint.getX() - 9, screenPoint.getY() - 9, 18, 18);
            g.drawImage(AppAssets.monument, screenPoint.getX() - 8, screenPoint.getY() - 8);

            g.setFill(landmarkTextFill);
            g.fillText("X" + l.getId(), screenPoint.getX() + 16, screenPoint.getY() + 4);
        }
    }

    private void drawPatient(GraphicsContext g, Patient patient) {
        PatientState patientState = patient.getPatientState();
        Point screenPoint = worldToCanvas(patient);

        Image img = AppAssets.patientWaiting;

        if (patientState == PatientState.ABANDONED || patientState == PatientState.ACCEPTED) {
            return;
        } else if (patientState == PatientState.OUTOFBOUNDS) {
            img = AppAssets.patientAbandoned;
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

        if (patient == programAlgorithm.getCurrentPatient()) {
            g.setFill(Color.BLUE);
            g.fillRect(
                    screenPoint.getX() - 9, screenPoint.getY() - 9,
                    32 + measureText(String.valueOf(patient.getId()))[0], 18
            );
        }

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
