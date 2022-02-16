package com.mobileplatform.frontend.form;

import com.mobileplatform.frontend.dto.LocationDto;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;

public class VehicleLocationPane extends JPanel {

    private final LocationDto[] locationDtos;

    public VehicleLocationPane(LocationDto[] locationDtos) {
        this.locationDtos = locationDtos;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawLine(getWidth()/2, 0, getWidth()/2, getHeight()); // Y axis
        g2d.drawLine(0, getHeight()/2, getWidth(), getHeight()/2); // X axis
        g2d.dispose();

        g2d = (Graphics2D) g.create();
        drawRealLocation(g2d);
        drawSlamLocation(g2d);
        g2d.dispose();
    }

    public void drawRealLocation(Graphics2D g2d) {

        g2d.setColor(Color.RED);
        GeneralPath path = new GeneralPath();
        int beginningXCoord = getWidth()/2; // to draw points relatively to the beginning of the coordinate frame
        int beginningYCoord = getHeight()/2;
        path.moveTo((float) beginningXCoord, (float) beginningYCoord); // start the path at the beginning of the coordinate frame
        for(LocationDto location : this.locationDtos) {
            double realXCoordinate = Math.abs(location.getRealXCoordinate() + beginningXCoord);
            double realYCoordinate = Math.abs(location.getRealYCoordinate()) + beginningYCoord;
            addPointToPath(g2d, path, realXCoordinate, realYCoordinate);
        }
        g2d.draw(path);
    }

    public void drawSlamLocation(Graphics2D g2d) {

        g2d.setColor(Color.BLUE);
        GeneralPath path = new GeneralPath();
        int beginningXCoord = getWidth()/2; // to draw points relatively to the beginning of the coordinate frame
        int beginningYCoord = getHeight()/2;
        path.moveTo((float) beginningXCoord, (float) beginningYCoord); // start the path at the beginning of the coordinate frame
        for(LocationDto location : this.locationDtos) {
            double slamXCoordinate = Math.abs(location.getSlamXCoordinate() + beginningXCoord);
            double slamYCoordinate = Math.abs(location.getSlamYCoordinate() + beginningYCoord);
            addPointToPath(g2d, path, slamXCoordinate, slamYCoordinate);
        }
        g2d.draw(path);
    }

    private void addPointToPath(Graphics2D g2d, GeneralPath path, double realXCoordinate, double realYCoordinate) {
        path.lineTo(realXCoordinate, realYCoordinate); // TODO - handle negative coordinates
        Polygon polygon = new Polygon();
        polygon.addPoint((int) realXCoordinate - 3, (int) realYCoordinate - 3);
        polygon.addPoint((int) realXCoordinate + 3, (int) realYCoordinate - 3);
        polygon.addPoint((int) realXCoordinate + 3, (int) realYCoordinate + 3);
        polygon.addPoint((int) realXCoordinate - 3, (int) realYCoordinate + 3);
        g2d.fillPolygon(polygon);
    }
}
