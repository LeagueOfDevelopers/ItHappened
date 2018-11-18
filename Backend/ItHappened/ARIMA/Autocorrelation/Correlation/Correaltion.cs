using System;
using MathNet.Numerics.Distributions;

namespace ARIMA.Autocorrelation.Correlation
{
    internal class Correaltion
    {
        private readonly double coefficient;
        private readonly int numSamples;
        private readonly int lag;
        public bool IsSignificant { get; }

        public Correaltion(double coefficient, int numSamples)
        {
            if (coefficient > 1 || coefficient < -1)
            {
                throw new ArgumentException("Correlation coefficient should be between -1 and 1");
            }
            this.coefficient = coefficient;
            this.numSamples = numSamples;
            IsSignificant = SignificanceTest();
        }

        public double Coefficient => coefficient;
        public int NumSamples => numSamples;

        private bool SignificanceTest()
        {
            var t = coefficient * Math.Sqrt(numSamples - lag - 2) / Math.Sqrt(1 - Math.Pow(coefficient, 2));
            const double alpha = 0.05;
            var tCritical = StudentT.InvCDF(0, 1, numSamples - lag - 2, alpha);
            return Math.Abs(t) < tCritical;
        }
    }
}
