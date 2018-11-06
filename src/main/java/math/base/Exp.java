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
 * exp(x)
 */
public final class Exp extends OneDFun {

    private static final DFunction exp = new Fun();

    public static final Exp op = new Exp();

    public Exp() {
    }

    @Override
    public DFunction fun() {
        return exp;
    }

    // exact override (optional)
    @Override
    public DFunction deriv1() {
        return exp;
    }

    private static final class Fun implements DFunction {
        @Override
        public double apply(double x) {
            return Math.exp(x);
        }

        @Override
        public String toString() {
            return "exp ";
        }
    }

    public String toString() {
        return "Exp(x) : exp(x)";
    }
}
