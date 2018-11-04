using System;
using MathNet.Numerics.Distributions;

namespace ARIMA.Autocorrelation.PearsonCorrelation
{
    internal class PearsonCorrealtion
    {
        private readonly double coefficient;
        private readonly int numSamples;
        public bool IsSignificant { get; }

        public PearsonCorrealtion(double coefficient, int numSamples)
        {
            this.coefficient = coefficient;
            this.numSamples = numSamples;
            IsSignificant = SignificanceTest();
        }

        public double Coefficient => coefficient;
        public int NumSamples => numSamples;

        private bool SignificanceTest()
        {
            var t = coefficient * Math.Sqrt(numSamples - 2) / Math.Sqrt(1 - Math.Pow(coefficient, 2));
            const double alpha = 0.05;
            var tCritical = StudentT.InvCDF(0, 1, numSamples - 2, alpha);
            return Math.Abs(t) < tCritical;
        }
    }
}
