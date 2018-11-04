using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ARIMA.Models
{
    internal class Sequence
    {
        private readonly List<double> data;
        private readonly double sum;

        public Sequence(List<double> data)
        {
            this.data = data;
            sum = 0;
            foreach (var elem in data)
            {
                sum += elem;
            }
        }

        public double this[int i] => data[i];
        public int Length() => data.Count;
        public double Sum() => sum;
        public double Mean() => sum / data.Count;

        public Sequence Sum(Sequence seq)
        {
            if (data.Count != seq.Length())
            {
                throw new ArgumentException("Dimensions of sequences should be same");
            }
            var result = new List<double>();
            for (var i = 0; i < seq.Length(); i++)
            {
                result.Add(data[i] + seq[i]);
            }
            return new Sequence(result);
        }

        public Sequence Sum(double constant)
        {
            var result = data.Select(x => x + constant).ToList();
            return new Sequence(result);
        }

        public Sequence Difference(Sequence seq)
        {
            if (data.Count != seq.Length())
            {
                throw new ArgumentException("Dimensions of sequences should be same");
            }
            var result = new List<double>();
            for (var i = 0; i < seq.Length(); i++)
            {
                result.Add(data[i] - seq[i]);
            }
            return new Sequence(result);
        }

        public Sequence Difference(double constant)
        {
            var result = data.Select(t => t - constant).ToList();
            return new Sequence(result);
        }

        public Sequence PowSequence(int degree)
        {
            var result = data.Select(t => Math.Pow(t, degree)).ToList();
            return new Sequence(result);
        }

        public Sequence Mult(Sequence seq)
        {
            if (data.Count != seq.Length())
            {
                throw new ArgumentException("Dimensions of sequences should be same");
            }
            var result = new List<double>();
            for (var i = 0; i < seq.Length(); i++)
            {
                result.Add(data[i] * seq[i]);
            }
            return new Sequence(result);
        }

        public Sequence Slice(int start, int end)
        {
            var result = new List<double>();
            for (var i = start; i < end; i++)
            {
                result.Add(data[i]);
            }
            return new Sequence(result);
        }
    }
}
