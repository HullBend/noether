package math.chart.demo;

import math.base.LinSpace;
import math.base.M;
import math.chart.Graph;

public class WitchOfAgnesi {

    private static final double[] data = new double[4096];
    private static final double dx = 10.0 / data.length;

    public static void main(String[] args) {
        initializeData();
        LinSpace lsp = M.linspace(-5.0,  5.0 - dx, data.length).allocate();
        for (int i = 1; i <= lsp.size(); ++i) {
            lsp.setValue(i, data[i - 1]);
        }

        Graph g = new Graph("Agnesi");
        g.plot("witch", lsp);
        g.show();
    }

    private static void initializeData() {
        for (int i = 0; i < data.length; ++i) {
            data[i] = witchFun(-5.0 + (i * dx));
        }
    }

    private static double witchFun(double x) {
        return 1.0 / (1.0 + (x * x));
    }
}
