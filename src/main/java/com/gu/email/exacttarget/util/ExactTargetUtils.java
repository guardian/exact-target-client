package com.gu.email.exacttarget.util;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: nbennett
 * Date: 19/11/12
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class ExactTargetUtils {

    public static String convertStreamToString(InputStream inputStream, String encoding) throws IOException {

        StringBuilder stringBuilder = new StringBuilder( Math.max(16, inputStream.available()) );
        char [] tmp = new char[4096];

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
            for( int cnt; ( cnt = inputStreamReader.read( tmp) ) > 0; )
                stringBuilder.append(tmp, 0, cnt);

        }  finally {
            inputStream.close();
        }
        return stringBuilder.toString();
    }
}
