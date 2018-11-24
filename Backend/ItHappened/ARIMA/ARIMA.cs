using System;
using System.Collections.Generic;
using ARIMA.Models;
using ItHappenedDomain.Domain;

namespace ARIMA
{
    public class ArimaModel
    {
        private int p; // столько предыдущих значений ряда используется для предсказаний
        private int q; // столько предыдущих значений ряда ошибок используется для предсказаний
        private int d; // порядок дифференцирования
        private int P; // 
        private int Q;
        private int D;
        
        public void fit(List<Event> data)
        {
            
        }

        private Sequence TransformData(List<Event> events)
        {
            throw new NotImplementedException();
        }
    }
}
