namespace ARIMA
{
    internal class ParameterSet
    {
        public int p { get; set; } // столько предыдущих значений ряда используется для предсказаний
        public int q { get; set; } // столько предыдущих значений ряда ошибок используется для предсказаний
        public int d { get; set; } // порядок дифференцирования
        public int P { get; set; }
        public int Q { get; set; }
        public int D { get; set; }
    }
}
