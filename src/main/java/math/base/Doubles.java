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
package math.base;

public final class Doubles {

    private static final Double ZERO = Double.valueOf(0.0d);

    public static Double of(double x) {
        if (x == 0.0d) {
            return ZERO;
        }
        return Double.valueOf(x);
    }

    public static void softmax(double[] x, double[] out) {
        double max = x[0];
        for (int i = 1; i < x.length; ++i) {
            max = Math.max(max, x[i]);
        }
        double s = 0.0;
        for (int i = 0; i < x.length; ++i) {
            double q = Math.exp(x[i] - max);
            s += q;
            out[i] = q;
        }
        s = 1.0 / s;
        for (int i = 0; i < x.length; ++i) {
            out[i] *= s;
        }
    }

    private Doubles() {
        throw new AssertionError();
    }
}
