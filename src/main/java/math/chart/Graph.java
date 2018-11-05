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
import java.util.NoSuchElementException;

import javax.swing.JFrame;

import math.base.DBiConsumer;
import math.base.DForEachBi;
import math.base.LinSpace;
import math.chart.Chart;
import math.chart.Shade;
import math.chart.Trace;
import math.chart.TracePoint;

/**
 * A basic curve diagram.
 */
public class Graph {

    private static final Shade[] shades = { Shade.RED, Shade.BLUE, Shade.BLACK, Shade.YELLOW, Shade.GREEN, Shade.ORANGE,
            Shade.CYAN, Shade.MAGENTA, Shade.DARK_GRAY };
    private int nextShade = 0;
    private int nextPlot = 1;

    private final JFrame frame = createFrame("");
    private final Chart chart;

    public Graph(String title) {
        if (title != null) {
            frame.setTitle(title);
        }
        chart = new Chart();
        frame.getContentPane().add(chart);
    }

    public Graph plot(LinSpace data) {
        return plot("curve-" + nextPlot++, data);
    }

    public Graph plot(String plotName, LinSpace data) {
        if (nextShade == shades.length) {
            nextShade = 0;
        }
        return plot(plotName, shades[nextShade++], data);
    }

    public Graph plot(String plotName, Shade color, LinSpace data) {
        if (!data.hasValues()) {
            throw new NoSuchElementException("no data");
        }
        final Trace trace = new Trace(plotName);
        trace.setColor(color.getColor());
        chart.addTrace(trace);
        DForEachBi feb = data.forEachBi();
        feb.forEachRemaining(new DBiConsumer() {
            @Override
            public void accept(double x, double y) {
                trace.addPoint(new TracePoint(x, y));
            }
        });
        return this;
    }

    public void show() {
        frame.setVisible(true);
    }

    public Graph useAntialiasing() {
        chart.setUseAntialiasing(true);
        return this;
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
