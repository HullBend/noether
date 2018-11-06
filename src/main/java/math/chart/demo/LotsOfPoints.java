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
package math.chart.demo;

import math.base.M;
import math.base.DFunction;
import math.base.LinSpace;
import math.chart.Graph;
import math.chart.Shade;

import java.util.Random;

public class LotsOfPoints {

    public static void main(String[] args) {

        final int size = 262144;
        final Random random = new Random();

        LinSpace lsp = M.compute(0.0, size - 1.0, size, new DFunction() {
            @Override
            public double apply(double x) {
                return (60.0 + random.nextDouble()) * x;
            }
        });

        Graph gr = new Graph("LotsOfPoints");
        gr.plot("Quite \"BIG\"", Shade.CYAN, lsp);
        gr.show();
    }
}
