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

import math.base.LinSpace;
import math.chart.Graph;
import math.chart.Shade;
import math.complex.ComplexArray;

public class FFTSinCosDemo1 {

    private static final double dt = 0.0009765625;
    // N = 1024 points in [0, 1)
    private static final int N = (int) (1.0 / dt);
    private static final double[] data = new double[N];

    public static void main(String[] args) {
        initializeData();

        ComplexArray raw = ComplexArray.naiveForwarDFT(data);
        ComplexArray shifted = raw.fftshift();

        // FFT-shifted power-spectrum density
        double[] psd = shifted.absSquaredScaled();
        LinSpace lsp1 = LinSpace.centeredIntIndexed(psd);

        // shifted + scaled original data
        double[] sinusoidScaled = getScaledOriginalData();
        LinSpace lsp2 = LinSpace.centeredIntIndexed(sinusoidScaled);

        // shifted + scaled reconstructed data
        double[] reconstructedScaled = getScaledReconstructedData(raw);
        LinSpace lsp3 = LinSpace.centeredIntIndexed(reconstructedScaled);

        Graph g = new Graph("SinCos-FFT power spectrum");
        g.plot("SinCos power spectrum", lsp1);
        g.plot("SinCos scaled & shifted", lsp2);
        g.plot("SinCos reconstructed", Shade.GREEN, lsp3);
        g.show();
    }

    private static void initializeData() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = sinCosFun((double) i / N);
        }
    }

    private static double[] getScaledOriginalData() {
        double[] scaled = new double[N];
        for (int i = 0; i < scaled.length; ++i) {
            scaled[i] = 10.0 * data[i];
        }
        return scaled;
    }

    private static double[] getScaledReconstructedData(ComplexArray raw) {
        ComplexArray reconstructed = ComplexArray.naiveInverseDFT(raw);
        double[] unscaled = reconstructed.re();
        double[] scaled = new double[unscaled.length];
        for (int i = 0; i < unscaled.length; ++i) {
            scaled[i] = 160.0 + 10.0 * unscaled[i];
        }
        return scaled;
    }

    private static double sinCosFun(double t) {
        // w1 = 50 Hz; w2 = 120 Hz
        return Math.sin(2.0 * Math.PI * 50.0 * t) + Math.cos(2.0 * Math.PI * 120.0 * t);
    }
}
