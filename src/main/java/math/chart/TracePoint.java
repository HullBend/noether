/*
 * Copyright 2018 SPZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package math.chart;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Set;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ICodeBlock;
import info.monitorenter.gui.chart.IPointPainter;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;

@SuppressWarnings("serial")
public class TracePoint extends Point2D.Double implements ITracePoint2D {

    /**
     * The reference to the listening <code>ITrace</code> which owns this point.
     * <p>
     * A trace point can only be contained in one trace!
     */
    private ITrace2D m_listener;

    /**
     * Scaled x value.
     */
    private double m_scaledX;

    /**
     * Scaled y value.
     */
    private double m_scaledY;

    protected TracePoint() {
    }

    public TracePoint(double x, double y) {
        super(x, y);
    }

    @Override
    public int compareTo(ITracePoint2D o) {
        double othX = o.getX();
        if (x < othX) {
            return -1;
        } else {
            if (x == othX) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public boolean addAdditionalPointPainter(IPointPainter<?> pointPainter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAdditionalPointPainter(IPointPainter<?> pointPainter) {
        return false;
    }

    @Override
    public Set<IPointPainter<?>> removeAllAdditionalPointPainters() {
        return Collections.emptySet();
    }

    @Override
    public Set<IPointPainter<?>> getAdditionalPointPainters() {
        return Collections.emptySet();
    }

    @Override
    public double getEuclidDistance(double xNormalized, double yNormalized) {
        double xDist = Math.abs(m_scaledX - xNormalized);
        double yDist = Math.abs(m_scaledY - yNormalized);
        return Math.sqrt((xDist * xDist) + (yDist * yDist));
    }

    @Override
    public ITrace2D getListener() {
        return m_listener;
    }

    @Override
    public double getManhattanDistance(double xNormalized, double yNormalized) {
        return Math.abs(m_scaledX - xNormalized) + Math.abs(m_scaledY - yNormalized);
    }

    @Override
    public double getManhattanDistance(ITracePoint2D point) {
        return getManhattanDistance(point.getX(), point.getY());
    }

    @Override
    public final double getScaledX() {
        return m_scaledX;
    }

    @Override
    public final double getScaledY() {
        return m_scaledY;
    }

    @Override
    public void setListener(ITrace2D listener) {
        m_listener = listener;
    }

    @Override
    public final void setScaledX(double scaledX) {
        m_scaledX = scaledX;
    }

    @Override
    public final void setScaledY(double scaledY) {
        m_scaledY = scaledY;
    }

    private void setLocationInternal(double xValue, double yValue) {
        super.setLocation(xValue, yValue);
    }

    @Override
    public void setLocation(final double xValue, final double yValue) {
        doSynchronized(new ICodeBlock<Object>() {
            public Object execute() {
                TracePoint.this.setLocationInternal(xValue, yValue);
                if (TracePoint.this.m_listener != null) {
                    TracePoint.this.m_listener.firePointChanged(TracePoint.this, ITracePoint2D.STATE_CHANGED);
                }
                return null;
            }
        });
    }

    private <T> T doSynchronized(final ICodeBlock<T> runSynchronized) {
        if (m_listener != null) {
            Chart2D chart = m_listener.getRenderer();
            if (chart != null) {
                // already connected to the chart: keep full locking order
                synchronized (chart) {
                    synchronized (m_listener) {
                        return runSynchronized.execute();
                    }
                }
            } else {
                // not connected to a chart by now
                synchronized (m_listener) {
                    return runSynchronized.execute();
                }
            }
        } else {
            // not connected to any trace now
            return runSynchronized.execute();
        }
    }
}
