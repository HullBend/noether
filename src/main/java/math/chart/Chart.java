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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ZoomableChart;

/**
 * A minimal chart.
 */
@SuppressWarnings("serial")
public class Chart extends ZoomableChart {

    private static final String ZOOM_RESET = "ZoomReset";

    private final Action zoomReset = new AbstractAction(ZOOM_RESET) {
        @Override
        public void actionPerformed(ActionEvent e) {
            zoomAll();
        }
    };

    public Chart() {
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ZOOM_RESET);
        getActionMap().put(ZOOM_RESET, zoomReset);
        setPointFinder(PointFinder.MANHATTAN);
        setToolTipType(Chart2D.ToolTipType.DATAVALUES);
        setUseAntialiasing(false);
    }
}
