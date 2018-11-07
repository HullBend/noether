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

public abstract class OneDFun {

    public final LinSpace eval(LinSpace vec) {
        return vec.eval(fun());
    }

    public final double value(double x) {
        return fun().apply(x);
    }

    /**
     * f o g where f is this
     */
    public final OneDFun compose(OneDFun g) {
        return new ComposedOneDFun(this, g);
    }

    /**
     * g o f where f is this
     */
    public final OneDFun andThen(OneDFun g) {
        return new ComposedOneDFun(g, this);
    }

    public abstract DFunction fun();

    public final OneDFun fstDerivative() {
        return new FirstDerivative(deriv1());
    }

    // Override deriv1() for exact implementations
    public DFunction deriv1() {
        return new NumericDeriv1(fun());
    }

    private static final class FirstDerivative extends OneDFun {

        private final DFunction deriv;

        FirstDerivative(DFunction deriv) {
            this.deriv = deriv;
        }

        @Override
        public DFunction fun() {
            return deriv;
        }
    }

    /**
     * Numerical finite difference approximation of the first derivative using
     * the forward difference estimate {@code f'(x) = (f(x + h) - f(x)) / h}.
     * <p>
     * Scaling of {@code h} is taken into account by {@code h} being based upon
     * the absolute magnitude of {@code x}.
     */
    private static final class NumericDeriv1 implements DFunction {

        private static final double DIFF_SCALE = 1.5805068191585264E-8;

        private final DFunction fun;

        NumericDeriv1(DFunction fun) {
            this.fun = fun;
        }

        @Override
        public double apply(double x) {
            double fx = fun.apply(x);
            double h = (x != 0.0) ? DIFF_SCALE * Math.abs(x) : DIFF_SCALE;

            double x_plus_h = x + h;
            // account for potential round-off errors
            h = x_plus_h - x;
            // new function value for advance in variable
            double fx_plus_h = fun.apply(x_plus_h);
            // estimated gradient
            return (fx_plus_h - fx) / h;
        }
    }

    /**
     * f o g
     */
    private static final class ComposedOneDFun extends OneDFun {

        private final ComposedDFunction fog;

        ComposedOneDFun(OneDFun f, OneDFun g) {
            fog = new ComposedDFunction(f.fun(), g.fun());
        }

        @Override
        public DFunction fun() {
            return fog;
        }

        @Override
        public String toString() {
            return fog.toString(); 
        }
    }

    /**
     * f o g
     */
    private static final class ComposedDFunction implements DFunction {

        private final DFunction f;
        private final DFunction g;

        ComposedDFunction(DFunction f, DFunction g) {
            this.f = f;
            this.g = g;
        }

        /**
         * f o g
         */
        @Override
        public double apply(double x) {
            return f.apply(g.apply(x));
        }

        @Override
        public String toString() {
            return "(" + f.toString() + ") o (" + g.toString() + ")";
        }
    }
}
