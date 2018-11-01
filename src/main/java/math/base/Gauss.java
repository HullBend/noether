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

/**
 * exp(-alpha * x^2)
 */
public final class Gauss extends OneDFun {

    private final double alpha;
    private final DFunction gauss;
    private final DFunction deriv1;

    public Gauss(double alpha) {
        if (alpha <= 0.0) {
            throw new IllegalArgumentException("alpha : " + alpha + " (must be strictly positive)");
        }
        this.alpha = alpha;
        this.gauss = new Fun(alpha);
        this.deriv1 = new Deriv1(alpha);
    }

    @Override
    public DFunction fun() {
        return gauss;
    }

    // exact override (optional)
    @Override
    public DFunction deriv1() {
        return deriv1;
    }

    private static final class Fun implements DFunction {
        private final double a;

        Fun(double alpha) {
            this.a = alpha;
        }

        @Override
        public double apply(double x) {
            return Math.exp(-a * (x * x));
        }
    }

    private static final class Deriv1 implements DFunction {
        private final double a;

        Deriv1(double alpha) {
            this.a = alpha;
        }

        @Override
        public double apply(double x) {
            return -2.0 * a * x * Math.exp(-a * (x * x));
        }
    }

    public String toString() {
        return "Gauss(alpha=" + alpha + ", x) : exp(-alpha * x^2)";
    }
}
