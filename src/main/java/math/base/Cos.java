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
 * cos x
 */
public final class Cos extends OneDFun {

    private static final DFunction cos = new Fun();
    private static final DFunction deriv1 = new Deriv1();

    public static final Cos op = new Cos();

    public Cos() {
    }

    @Override
    public DFunction fun() {
        return cos;
    }

    // exact override (optional)
    @Override
    public DFunction deriv1() {
        return deriv1;
    }

    private static final class Fun implements DFunction {
        @Override
        public double apply(double x) {
            return Math.cos(x);
        }
    }

    private static final class Deriv1 implements DFunction {
        @Override
        public double apply(double x) {
            return -Math.sin(x);
        }
    }

    public String toString() {
        return "Cos(x) : cos x";
    }
}
