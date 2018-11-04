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

import java.util.ArrayDeque;
import java.util.Iterator;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.ITracePoint2D;
import info.monitorenter.gui.chart.traces.ATrace2D;

@SuppressWarnings("serial")
public class Trace extends ATrace2D implements ITrace2D {

    protected final ArrayDeque<ITracePoint2D> m_points = new ArrayDeque<ITracePoint2D>(8192);

    public Trace() {
        this(Trace.class.getName() + "-" + ATrace2D.getInstanceCount());
    }

    public Trace(String name) {
        super.setName(name);
    }

    @Override
    protected boolean addPointInternal(ITracePoint2D p) {
        if (p != null) {
            return m_points.add(p);
        }
        return false;
    }

    @Override
    public final int getMaxSize() {
        return Integer.MAX_VALUE - 8;
    }

    @Override
    public final int getSize() {
        return m_points.size();
    }

    @Override
    public boolean isEmpty() {
        return m_points.isEmpty();
    }

    @Override
    public Iterator<ITracePoint2D> iterator() {
        return m_points.iterator();
    }

    // @Override
    public Iterator<ITracePoint2D> descendingIterator() {
        return m_points.descendingIterator();
    }

    @Override
    protected final void removeAllPointsInternal() {
        m_points.clear();
    }

    @Override
    protected ITracePoint2D removePointInternal(ITracePoint2D point) {
        if (point != null && m_points.removeFirstOccurrence(point)) {
            return point;
        }
        return null;
    }
}
