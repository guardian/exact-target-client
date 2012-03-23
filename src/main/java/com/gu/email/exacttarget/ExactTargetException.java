package com.gu.email.exacttarget;

public class ExactTargetException extends Exception
{
    public ExactTargetException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public ExactTargetException( Throwable cause )
    {
        super( cause );
    }

    public ExactTargetException( String message )
    {
        super( message );
    }
}
