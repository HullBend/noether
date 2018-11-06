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
 * A constant {@code c}: x -> c
 */
public final class Const extends OneDFun {

    private final double c;
    private final DFunction const_;

    private static final DFunction deriv1 = new Deriv1();

    public Const(double c) {
        this.c = c;
        this.const_ = new Fun(c);
    }

    @Override
    public DFunction fun() {
        return const_;
    }

    // exact override (optional)
    @Override
    public DFunction deriv1() {
        return deriv1;
    }

    private static final class Fun implements DFunction {

        private final double const_;

        Fun(double const_) {
            this.const_ = const_;
        }

        @Override
        public double apply(double x) {
            return const_;
        }

        @Override
        public String toString() {
            return Double.toString(const_);
        }
    }

    private static final class Deriv1 implements DFunction {
        @Override
        public double apply(double x) {
            return 0.0;
        }
    }

    public String toString() {
        return "Const(c=" + c + ", x) : x -> c";
    }
}
