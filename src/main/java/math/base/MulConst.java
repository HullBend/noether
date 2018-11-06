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
 * x -> ax
 */
public final class MulConst extends OneDFun {

    private final double factor;
    private final DFunction mul;

    private final Deriv1 deriv1;

    public MulConst(double factor) {
        this.factor = factor;
        this.mul = new Fun(factor);
        this.deriv1 = new Deriv1(factor);
    }

    @Override
    public DFunction fun() {
        return mul;
    }

    // exact override (optional)
    @Override
    public DFunction deriv1() {
        return deriv1;
    }

    private static final class Fun implements DFunction {

        private final double a;

        Fun(double a) {
            this.a = a;
        }

        @Override
        public double apply(double x) {
            return a * x;
        }
    }

    private static final class Deriv1 implements DFunction {

        private final double a;

        Deriv1(double a) {
            this.a = a;
        }

        @Override
        public double apply(double x) {
            return a;
        }
    }

    public String toString() {
        return "MulConst(a=" + factor + ") : x -> ax";
    }
}
