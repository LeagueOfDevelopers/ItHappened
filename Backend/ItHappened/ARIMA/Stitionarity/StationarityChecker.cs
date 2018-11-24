using ARIMA.Models;
using MathNet.Numerics.LinearAlgebra.Double;
using MathNet.Numerics.LinearAlgebra.Factorization;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace ARIMA.Stitionarity
{
    internal class StationarityChecker
    {
        public bool CheckEventStationarity(Sequence timeseries, double[] ar)
        {
            if (timeseries.Length() == 0)
            {
                return true;
            }
            double[] arCoeffs = new double[ar.Length + 1];
            arCoeffs[0] = 1.0;
            for (var i = 0; i < ar.Length; i++)
            {
                arCoeffs[i + 1] = ar[i];
            }

            IEnumerable<double> roots = Roots(arCoeffs);

            if (roots.Any(x => x <= 1.0))
            {
                return false;
            }
            return true;
        }

        private IEnumerable<double> Roots(double[] coefs)
        {
            Complex[] matrix = FindRoots(coefs);
            return matrix.Select(x => x.Magnitude);
        }

        private Complex[] FindRoots(double[] coefs)
        {
            var N = coefs.Length - 1;
            DenseMatrix matrix = new DenseMatrix(N);
            var a = coefs[N];

            for (var i = 0; i < N; i++)
            {
                matrix[i, N - 1] = -coefs[i] / a;
                if (i == 0) continue;
                matrix[i, i - 1] = 1;
            }

            Evd<double> evd = matrix.Evd();
            MathNet.Numerics.LinearAlgebra.Vector<Complex> values = evd.EigenValues;
            return values.AsArray();
        }
    }
}
