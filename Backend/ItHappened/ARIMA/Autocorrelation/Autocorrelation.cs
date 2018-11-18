using ARIMA.Autocorrelation.Correlation;

namespace ARIMA.Autocorrelation
{
    internal class Autocorrelation
    {
        public int Lag { get; }
        private readonly Correaltion correlation;
        public bool IsSignificant => correlation.IsSignificant;

        public Autocorrelation(int lag, Correaltion corr)
        {
            Lag = lag;
            correlation = corr;
        }
    }
}
