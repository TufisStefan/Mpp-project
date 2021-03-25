using System;

namespace C_Sharp.domain
{
    public interface IValidator<T>
    {
        void validate(T entity);
    }

    public class ValidationException : ApplicationException
    {
        public ValidationException(string message) : base(message)
        {
        }
    }
}