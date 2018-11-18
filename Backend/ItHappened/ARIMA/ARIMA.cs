using ARIMA.Models;
using ARIMA.Autocorrelation;
using System.Collections.Generic;
using System;

namespace ARIMA
{
    public class ArimaModel
    {
        private bool IsFitted = false;
        private ParameterSet parameters;
        private List<double> valuesCoefficients;
        private List<double> errorCoefficients;
        
        public void fit(Sequence data)
        {
            parameters = EstimateParameters(data);
            // model coefficients estimation
            IsFitted = true;
        }

        public double predict(Sequence data, int n)
        {
            if (!IsFitted)
            {
                throw new Exception("Model should be fitted before predicting");
            }

            for (var i = 0; i < n; i++)
            {
                
            }
        }

        private ParameterSet EstimateParameters(Sequence data)
        {
            var set = new ParameterSet();
            set.d = new AutocorrGraphComputer().FindLastSignificantLag(data).Lag;
            throw new NotImplementedException();
            // set.p = 
            // set.q = 
            // set.P = 
            // set.Q = 
            // set.D =
            return set;
        }
    }
}
