using System;
using ARIMA.Autocorrelation.Correlation;
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
                    data.Slice(lag, data.Length()), data.Mean());
                if (corr.IsSignificant)
                {
                    lastSignificantLag = new Autocorrelation(lag, corr);
                }
            }
            return lastSignificantLag;
        }

        private Correaltion ComputePearsonCorrealtion(Sequence seq1, Sequence seq2, double mean)
        {
            if (seq1.Length() != seq2.Length())
            {
                throw new ArgumentException("Dimensions of sequences must be same");
            }
            var counter = seq1.Difference(mean).Mult(seq2.Difference(mean)).Sum();
            var divider = seq1.Difference(mean).PowSequence(2).Sum();
            return new Correaltion(counter / divider, seq1.Length());
        }
    }
}
