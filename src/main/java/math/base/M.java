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

public final class M {

    public static LinSpace linspace(double start, double end) {
        return LinSpace.linspace(start, end);
    }

    public static LinSpace linspace(double start, double end, int numberOfPoints) {
        return LinSpace.linspace(start, end, numberOfPoints);
    }

    public static Abs abs() {
        return Abs.op;
    }

    public static Cos cos() {
        return Cos.op;
    }

    public static Exp exp() {
        return Exp.op;
    }

    public static ID id() {
        return ID.op;
    }

    public static Inv inv() {
        return Inv.op;
    }

    public static Ln ln() {
        return Ln.op;
    }

    public static Neg neg() {
        return Neg.op;
    }

    public static Sin sin() {
        return Sin.op;
    }

    public static Const constant(double c) {
        return new Const(c);
    }

    public static Gauss gauss(double alpha) {
        return new Gauss(alpha);
    }

    private M() {
        throw new AssertionError();
    }
}
