import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

class Points {
    public int x, y;

    public Points(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class ConvexHullAlgo {
    public static List<Point> bruteForce(List<Point> points) {
        int n = points.size();
        List<Point> convexHull = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }

                boolean valid = true;
                for (int k = 0; k < n; k++) {
                    if (k == i || k == j) {
                        continue;
                    }

                    int crossProduct = crossProduct(points.get(i), points.get(j), points.get(k));
                    if (crossProduct > 0) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    convexHull.add(points.get(i));
                    convexHull.add(points.get(j));
                }
            }
        }

        return convexHull;
    }

    private static int crossProduct(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p3.x - p1.x) * (p2.y - p1.y);
    }


    public static List<Point> jarvisMarch(List<Point> points) {
        if (points.size() < 3) {
            return points;
        }

        List<Point> convexHull = new ArrayList<>();

        Point pivot = findPivot(points);
        convexHull.add(pivot);

        do {
            Point endpoint = points.get(0);

            for (int i = 1; i < points.size(); i++) {
                if (endpoint.equals(pivot) || isLeftTurn(pivot, endpoint, points.get(i))) {
                    endpoint = points.get(i);
                }
            }

            convexHull.add(endpoint);
            pivot = endpoint;

        } while (!pivot.equals(convexHull.get(0)));

        return convexHull;
    }

    private static Point findPivot(List<Point> points) {
        return points.stream().min(Comparator.comparing(Point::getY).thenComparing(Point::getX)).orElseThrow();
    }

    private static boolean isLeftTurn(Point p1, Point p2, Point p3) {
        int crossProduct = crossProduct(p1, p2, p3);
        return crossProduct > 0;
    }

    public static List<Point> grahamScan(List<Point> points) {
        if (points.size() < 3) {
            return points;
        }

        // Sort points based on polar angle with respect to the pivot point
        Point pivot = findPivot(points);
        points.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.y - pivot.y, p1.x - pivot.x);
            double angle2 = Math.atan2(p2.y - pivot.y, p2.x - pivot.x);
            return Double.compare(angle1, angle2);
        });

        List<Point> convexHull = new ArrayList<>();

        convexHull.add(points.get(0));
        convexHull.add(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (convexHull.size() > 1 && !isLeftTurn(convexHull.get(convexHull.size() - 2),
                    convexHull.get(convexHull.size() - 1), points.get(i))) {
                convexHull.remove(convexHull.size() - 1);
            }
            convexHull.add(points.get(i));
        }

        return convexHull;
    }

    public static List<Point> quickElimination(List<Point> points) {
        // Find extreme points
        Point minX = points.stream().min(Comparator.comparingInt(p -> p.x)).orElseThrow();
        Point maxX = points.stream().max(Comparator.comparingInt(p -> p.x)).orElseThrow();

        // Sort points based on x-coordinate
        points.sort(Comparator.comparingInt(p -> p.x));

        List<Point> upperHull = new ArrayList<>();
        List<Point> lowerHull = new ArrayList<>();

        // Add extreme points to upper and lower hulls
        upperHull.add(minX);
        lowerHull.add(minX);

        int n = points.size();

        // Upper Hull
        for (int i = 1; i < n; i++) {
            while (upperHull.size() >= 2 && isRightTurn(upperHull.get(upperHull.size() - 2),
                    upperHull.get(upperHull.size() - 1), points.get(i))) {
                upperHull.remove(upperHull.size() - 1);
            }
            upperHull.add(points.get(i));
        }

        // Lower Hull
        for (int i = n - 2; i >= 0; i--) {
            while (lowerHull.size() >= 2 && isRightTurn(lowerHull.get(lowerHull.size() - 2),
                    lowerHull.get(lowerHull.size() - 1), points.get(i))) {
                lowerHull.remove(lowerHull.size() - 1);
            }
            lowerHull.add(points.get(i));
        }

        // Add extreme points to upper and lower hulls
        upperHull.add(maxX);
        lowerHull.add(maxX);

        // Combine upper and lower hulls
        upperHull.addAll(lowerHull.subList(2, lowerHull.size() - 1));

        return upperHull;
    }

    private static boolean isRightTurn(Point p1, Point p2, Point p3) {
        int crossProduct = crossProduct(p1, p2, p3);
        return crossProduct < 0;
    }

    public static List<Point> incremental(List<Point> points) {
        // Sort the points by x-coordinate
        Collections.sort(points, Comparator.comparingInt(point -> point.x));

        // Create a stack to store the convex hull
        Stack<Point> hull = new Stack<>();

        // Add the leftmost point to the convex hull
        hull.push(points.get(0));

        // Process each point in order
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);

            // Remove points that make a right turn with the new point
            while (hull.size() > 1 && isRightTurn(hull.get(hull.size() - 2), hull.peek(), current)) {
                hull.pop();
            }

            // Add the new point to the convex hull
            hull.push(current);
        }

        // Convert the stack to a list and return
        return new ArrayList<>(hull);
    }

}

class CHull extends JFrame {
    private List<Point> points = new ArrayList<>();
    private JTextArea outputArea;
    private ConvexHullAlgorithm selectedAlgorithm;

    public CHull() {
        setTitle("Convex Hull Algorithms");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        DrawingPanel drawingPanel = new DrawingPanel();
        panel.add(drawingPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        addAlgorithmButton(buttonPanel, "Brute Force", ConvexHullAlgo::bruteForce);
        addAlgorithmButton(buttonPanel, "Jarvis March", ConvexHullAlgo::jarvisMarch);
        addAlgorithmButton(buttonPanel, "Graham Scan", ConvexHullAlgo::grahamScan);
        addAlgorithmButton(buttonPanel, "Quick Elimination", ConvexHullAlgo::quickElimination);
        addAlgorithmButton(buttonPanel, "Incremental", ConvexHullAlgo::incremental);

        JButton inputPointsButton = new JButton("Input Points");
        inputPointsButton.addActionListener(e -> {
            inputPoints();

            long startTime = System.currentTimeMillis();
            List<Point> convexHull = selectedAlgorithm.computeConvexHull(new ArrayList<>(points));
            long endTime = System.currentTimeMillis();

            long elapsedTime = endTime - startTime;

            display(convexHull, elapsedTime);
            drawingPanel.repaint();
        });
        buttonPanel.add(inputPointsButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        add(panel);
    }

    private void addAlgorithmButton(JPanel panel, String label, ConvexHullAlgorithm algorithm) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            selectedAlgorithm = algorithm;
            List<Point> convexHull = selectedAlgorithm.computeConvexHull(new ArrayList<>(points));
            display(convexHull, 0);
            repaint();
        });
        panel.add(button);
    }

    private void display(List<Point> convexHull, double elapsedTime) {
        StringBuilder output = new StringBuilder("Convex Hull Points:\n");
        for (Point point : convexHull) {
            output.append("(").append(point.x).append(", ").append(point.y).append(")\n");
        }
        output.append("\nTime taken: ").append(String.format("%.3f", elapsedTime)).append(" milliseconds");
        outputArea.setText(output.toString());
    }

    private void inputPoints() {
        points.clear();
        int numberOfPoints = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of points:"));
        for (int i = 0; i < numberOfPoints; i++) {
            int x = Integer.parseInt(JOptionPane.showInputDialog("Enter x coordinate for point " + (i + 1) + ":"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Enter y coordinate for point " + (i + 1) + ":"));
            points.add(new Point(x, y));
        }
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
            for (Point point : points) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                drawPoint(g, point.x, point.y, Color.BLACK);
            }

            if (selectedAlgorithm != null) {
                // Draw convex hull using the selected algorithm
                long startTime = System.nanoTime();
                List<Point> convexHull = selectedAlgorithm.computeConvexHull(new ArrayList<>(points));
                long endTime = System.nanoTime();

                long elapsedTimeInNanos = endTime - startTime;
                elapsedTimeInNanos = elapsedTimeInNanos/10000;
                g.setColor(Color.RED);

                int scale = 20;
                int translateX = getWidth() / 2;  // Translate to the center horizontally
                int translateY = getHeight() / 2;  // Translate to the center vertically

                for (int i = 0; i < convexHull.size() - 1; i++) {
                    g.drawLine(convexHull.get(i).x * scale + translateX, convexHull.get(i).y * scale + translateY,
                            convexHull.get(i + 1).x * scale + translateX, convexHull.get(i + 1).y * scale + translateY);
                }
                g.drawLine(convexHull.get(convexHull.size() - 1).x * scale + translateX,
                        convexHull.get(convexHull.size() - 1).y * scale + translateY,
                        convexHull.get(0).x * scale + translateX, convexHull.get(0).y * scale + translateY);

                display(convexHull, (long) elapsedTimeInNanos);
            }
        }

        private void drawGrid(Graphics g) {
            int scale = 16;
            int width = getWidth();
            int height = getHeight();

            // vertical grid lines
            for (int x = 0; x <= width; x += scale) {
                g.drawLine(x, 0, x, height);
            }

            // horizontal grid lines
            for (int y = 0; y <= height; y += scale) {
                g.drawLine(0, y, width, y);
            }
        }

        private void drawPoint(Graphics g, int x, int y, Color color){
            g.setColor(color);
            int scale = 20;
            int translateX = getWidth() / 2;  // Translate to the center horizontally
            int translateY = getHeight() / 2;  // Translate to the center vertically
            g.fillOval(x * scale + translateX - 5, y * scale + translateY - 5, 10, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CHull app = new CHull();
            app.setVisible(true);
        });
    }

    interface ConvexHullAlgorithm {
        List<Point> computeConvexHull(List<Point> points);
    }
}