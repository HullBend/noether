package math.chart;

import java.awt.Graphics;
import java.util.ArrayList;

import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.painters.ATracePainter;

/*
 * A modified version of info.monitorenter.gui.chart.traces.painters.TracePainterPolyline
 * that uses less memory.
 */
@SuppressWarnings("serial")
final class TracePainterPolylineOpt extends ATracePainter {

    /* list of x coordinates collected in one paint iteration */
    private ArrayList<Integer> m_xPoints;

    /* list of y coordinates collected in one paint iteration */
    private ArrayList<Integer> m_yPoints;

    TracePainterPolylineOpt() {
    }

    @Override
    public void endPaintIteration(Graphics g2d) {
        if (g2d != null) {
            int count = 0;
            int[] x = new int[m_xPoints.size() + 1];
            for (Integer xpoint : m_xPoints) {
                x[count] = xpoint.intValue();
                count++;
            }
            x[count] = super.getPreviousX();

            count = 0;
            int[] y = new int[m_yPoints.size() + 1];
            for (Integer ypoint : m_yPoints) {
                y[count] = ypoint.intValue();
                count++;
            }
            y[count] = super.getPreviousY();

            g2d.drawPolyline(x, y, x.length);
        }
    }

    @Override
    public void paintPoint(int absoluteX, int absoluteY, int nextX, int nextY, Graphics g, ITracePoint2D original) {
        super.paintPoint(absoluteX, absoluteY, nextX, nextY, g, original);
        m_xPoints.add(Integer.valueOf(absoluteX));
        m_yPoints.add(Integer.valueOf(absoluteY));
    }

    @Override
    public void startPaintIteration(Graphics g2d) {
        super.startPaintIteration(g2d);
        if (m_xPoints == null) {
            m_xPoints = new ArrayList<Integer>(8192);
        } else {
            m_xPoints.clear();
        }
        if (m_yPoints == null) {
            m_yPoints = new ArrayList<Integer>(8192);
        } else {
            m_yPoints.clear();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        TracePainterPolylineOpt that = (TracePainterPolylineOpt) obj;
        if (this.m_xPoints != null && that.m_xPoints != null) {
            if (this.m_xPoints.size() != that.m_xPoints.size()) {
                return false;
            }
        }
        if (this.m_yPoints != null && that.m_yPoints != null) {
            if (this.m_yPoints.size() != that.m_yPoints.size()) {
                return false;
            }
        }
        return (this.m_xPoints == that.m_xPoints && this.m_yPoints == that.m_yPoints);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((m_xPoints == null) ? 0 : m_xPoints.hashCode());
        result = prime * result + ((m_yPoints == null) ? 0 : m_yPoints.hashCode());
        return result;
    }
}
