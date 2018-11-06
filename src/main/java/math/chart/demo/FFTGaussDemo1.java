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

import math.base.Gauss;
import math.base.LinSpace;
import math.base.M;
import math.chart.Graph;
import math.complex.ComplexArray;

public class FFTGaussDemo1 {

    public static void main(String[] args) {

        final int N = 65;
        int lower = -(N - 1) / 2;
        @SuppressWarnings("unused")
        int upper = (N % 2 != 0) ? (N - 1) / 2 - 1 : (N - 1) / 2;

        Graph graph = new Graph("Gauss-DFT (" + (N - 1) + " points in [" + lower + "," + upper + "])"); // .useAntialiasing();

        LinSpace lsp1 = M.linspace(-10.0, 10.0, N);
        lsp1 = lsp1.sliceTo(N - 1);
        lsp1 = new Gauss(1).eval(lsp1);

        ComplexArray ca = ComplexArray.naiveForwarDFT(lsp1.values());
        double[] abs = ca.re();
        LinSpace lsp2 = LinSpace.centeredIntIndexed(abs);

        graph.plot("Gauss", lsp1);
        graph.plot("Fourier modes", lsp2);
        graph.show();
    }
}
