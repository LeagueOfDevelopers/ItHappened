using ARIMA.Autocorrelation.PearsonCorrelation;

namespace ARIMA.Autocorrelation
{
    internal class Autocorrelation
    {
        public int Lag { get; }
        private readonly PearsonCorrealtion correlation;
        public bool IsSignificant => correlation.IsSignificant;

        public Autocorrelation(int lag, PearsonCorrealtion corr)
        {
            Lag = lag;
            correlation = corr;
        }
    }
}
