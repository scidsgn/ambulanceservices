package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.graph.Graph;
import aisd.zesp.ambulanceservices.main.Hospital;
import aisd.zesp.ambulanceservices.main.State;
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

    public void draw() {
        GraphicsContext g = getGraphicsContext2D();

        g.setFill(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (state != null) {
            Graph<Point> graph = state.getConnectionsGraph();

            for (Hospital h : state.getHospitalList()) {
                g.setFill(Color.WHITE);
                g.fillRect(h.getX(), h.getY(), 4, 4);
            }

            List<Point> graphNodes = graph.getNodes();
            g.beginPath();
            for (int i = 0; i < graphNodes.size(); i++) {
                for (int j = i; j < graphNodes.size(); j++) {
                    Point p1 = graphNodes.get(i);
                    Point p2 = graphNodes.get(j);
                    Double length = graph.getLength(p1, p2);

                    if (length != null) {
                        g.moveTo(p1.getX(), p1.getY());
                        g.lineTo(p2.getX(), p2.getY());
                    }
                }
            }
            g.stroke();
        }
    }
}
