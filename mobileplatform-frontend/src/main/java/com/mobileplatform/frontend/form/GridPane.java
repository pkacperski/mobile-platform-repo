package com.mobileplatform.frontend.form;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class GridPane extends JPanel {

//        @Override
//        public Dimension getPreferredSize() {
//            return new Dimension(300, 300); // TODO - ?w ustawieniach JPanelu w MainForm.form?
//        }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight()); // os Y
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2); // os X
        g2d.dispose();

        // I don't trust you - ???
        g2d = (Graphics2D) g.create();
        drawRealLocation(g2d, this);
        drawSlamLocation(g2d, this);
        g2d.dispose();
    }

    public void drawRealLocation(Graphics2D g2d, JComponent parent) {
        g2d.setColor(Color.RED);

        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        // TODO - narysowac 2 kropki: rzeczywiste i SLAMowe polozenie robota
        path.lineTo((float) parent.getWidth() / 2, (float) parent.getHeight() / 2);
        Polygon polygon = new Polygon();
        polygon.addPoint(parent.getWidth() / 2 - 5, parent.getHeight() / 2 - 5);
        polygon.addPoint(parent.getWidth() / 2 + 5, parent.getHeight() / 2 - 5);
        polygon.addPoint(parent.getWidth() / 2 + 5, parent.getHeight() / 2 + 5);
        polygon.addPoint(parent.getWidth() / 2 - 5, parent.getHeight() / 2 + 5);
        g2d.fillPolygon(polygon);
        path.lineTo(156, 19);
        Polygon polygon2 = new Polygon();
        polygon2.addPoint(156 - 5, 19 - 5);
        polygon2.addPoint(156 + 5, 19 - 5);
        polygon2.addPoint(156 + 5, 19 + 5);
        polygon2.addPoint(156 - 5, 19 + 5);
        g2d.fillPolygon(polygon2);
        g2d.draw(path);
//            path.curveTo(xPos + xDiff, 0, xPos, height, xPos + xDiff, height);
//            xPos += xDiff;
//            path.curveTo(xPos + xDiff, height, xPos, 0, xPos + xDiff, 0);
//            xPos += xDiff;
//            path.curveTo(xPos + xDiff, 0, xPos, height, xPos + xDiff, height);
//            xPos += xDiff;
//            path.curveTo(xPos + xDiff, height, xPos, 0, xPos + xDiff, 0);
//            g2d.draw(path);
    }

    public void drawSlamLocation(Graphics2D g2d, JComponent parent) {
        g2d.setColor(Color.GREEN);

        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.lineTo((float) parent.getWidth() / 4, (float) parent.getHeight() / 4);
        Polygon polygon = new Polygon();
        polygon.addPoint(parent.getWidth() / 4 - 5, parent.getHeight() / 4 - 5);
        polygon.addPoint(parent.getWidth() / 4 + 5, parent.getHeight() / 4 - 5);
        polygon.addPoint(parent.getWidth() / 4 + 5, parent.getHeight() / 4 + 5);
        polygon.addPoint(parent.getWidth() / 4 - 5, parent.getHeight() / 4 + 5);
        g2d.fillPolygon(polygon);
        path.lineTo(181, 39);
        Polygon polygon2 = new Polygon();
        polygon2.addPoint(181 - 5, 39 - 5);
        polygon2.addPoint(181 + 5, 39 - 5);
        polygon2.addPoint(181 + 5, 39 + 5);
        polygon2.addPoint(181 - 5, 39 + 5);
        g2d.fillPolygon(polygon2);
        g2d.draw(path);
    }
}
