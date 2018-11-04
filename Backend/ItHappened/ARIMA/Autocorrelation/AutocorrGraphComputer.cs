using System;
using ARIMA.Autocorrelation.PearsonCorrelation;
using ARIMA.Models;

namespace ARIMA.Autocorrelation
{
    internal class AutocorrGraphComputer
    {
        public Autocorrelation FindLastSignificantLag(Sequence data)
        {
            Autocorrelation lastSignificantLag = null;
            for (var lag = 0; lag < data.Length() - 2; lag++)
            {
                var corr = ComputePearsonCorrealtion(data.Slice(0, data.Length() - lag),
                    data.Slice(lag, data.Length()));
                if (corr.IsSignificant)
                {
                    lastSignificantLag = new Autocorrelation(lag, corr);
                }
            }
            return lastSignificantLag;
        }

        private PearsonCorrealtion ComputePearsonCorrealtion(Sequence seq1, Sequence seq2)
        {
            if (seq1.Length() != seq2.Length())
            {
                throw new ArgumentException("Dimensions of sequences must be same");
            }
            var counter = seq1.Difference(seq1.Mean()).Mult(seq2.Difference(seq2.Mean())).Sum();
            var divider = Math.Sqrt(seq1.Difference(seq1.Mean()).PowSequence(2).Sum()) *
                          Math.Sqrt(seq2.Difference(seq2.Mean()).PowSequence(2).Sum());
            return new PearsonCorrealtion(counter / divider, seq1.Length());
        }
    }
}
