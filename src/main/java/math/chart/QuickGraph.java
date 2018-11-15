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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.JFrame;

/**
 * Quick, simple (and inflexible) graph.
 */
public class QuickGraph {

    private final JFrame frame = createFrame("");

    public QuickGraph(String title, double[] axis, double[] values) {
        Trace trace = setup(title);
        for (int i = 0; i < axis.length; ++i) {
            trace.addPoint(new TracePoint(axis[i], values[i]));
        }
        show();
    }

    public QuickGraph(String title, List<Point2D.Double> points) {
        Trace trace = setup(title);
        for (Point2D.Double point : points) {
            trace.addPoint(new TracePoint(point.x, point.y));
        }
        show();
    }

    private Trace setup(String title) {
        if (title != null) {
            frame.setTitle(title);
        }
        Chart chart = new Chart();
        frame.getContentPane().add(chart);
        Trace trace = new Trace("values");
        trace.setColor(Shade.BLUE.getColor());
        chart.addTrace(trace);
        return trace;
    }

    private void show() {
        frame.setVisible(true);
    }

    private static JFrame createFrame(String title) {
        // create a frame
        JFrame frame = new JFrame(title);
        // set a default size
        frame.setSize(1000, 750);
        // enable close button
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // center frame
        frame.setLocationRelativeTo(null);
        return frame;
    }
}
