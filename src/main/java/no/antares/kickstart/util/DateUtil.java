package no.antares.kickstart.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final String DATE_ONLY_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    
    /** convert string with format DATE_ONLY_FORMAT (dd.MM.yyyy) */
    public static Date string2date(String date) throws ParseException {
        if (date == null)
            return null;
        // midday to avoid being between dates
        return parseDate( date + " 12:00:00", DATE_TIME_FORMAT );
    }
    
    /** gives a string with format DATE_TIME_FULL_FORMAT (dd.MM.yyyy HH:mm:ss) */
    public static String dateTimeString(java.util.Date date){
        if (date == null)
            return "";
        return formatDate( date, DATE_TIME_FORMAT );
    }

    /** Formats a date according to a dateFormat string */
    public static String formatDate( Date date, String dateFormat ) {
    	DateFormat formatter = new SimpleDateFormat( dateFormat );
    	return formatter.format( date );
    }

    /** Parses a string into a date according to a given dateFormat */
    public static Date parseDate( String dateString, String dateFormat ) throws ParseException {
    	DateFormat formatter = new SimpleDateFormat( dateFormat );
    	return formatter.parse( dateString );
    }

    // remainders belong in dao-layer
    /**
     * This method returns a string that represents a java.sql.date. The string is represented as:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * @since 1.0
     
    public static String shortDate2String(java.util.Date date){
        if (date == null)
            return "";
        // Format the current time.
        return formatDate( date, STANDARD_DATE_FORMAT );
    }*/

    /**
     * This method returns a string that represents a timestamp. The string is represented as:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * @since 1.0
     
    public static String timestamp2String (java.sql.Timestamp tms){
        if (tms == null)
            return "";
        java.util.Date date = new java.util.Date(tms.getTime());
        return formatDate( date, DATE_FORMAT );
    }*/

    /**
     * This method returns a java.sql.date that represents the incoming string. The string must be in the form:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * Return NULL if something goes wrong
     * @since 1.0
    
    public static java.sql.Date string2SqlDate(String str){
        try{
        	java.util.Date date = parseDate( str, DATE_FORMAT );
            // Parse the string back into a Date.
            return new java.sql.Date( date.getTime( ) );
          
        }catch (ParseException e){
            return null;
        }
    } */

    /**
     * This method returns a timestamp that represents the incoming string. The string must be in the form:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * Return NULL if something goes wrong
     * @since 1.0
     
    public static java.sql.Timestamp string2Timestamp(String str){
        try{
        	java.util.Date date = parseDate( str, DATE_FORMAT );
        	return new java.sql.Timestamp( date.getTime( ) );
        }catch ( ParseException e){
            return null;
        }
    }*/
    
    /**
     * This method returns a timestamp that represents the incoming string. The string must be in the form:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * Return NULL if something goes wrong
     * @since 1.0
     
    public static java.sql.Timestamp string2DateTime(String str){
        try{
            java.util.Date date = parseDate(str, DATE_TIME_FORMAT );
            return new java.sql.Timestamp(date.getTime());
        }catch ( ParseException e){
            return null;
        }
    }*/
    
     /**
     * This method returns a timestamp that represents the incoming string. The string must be in the form:
     * dd.MM.yyyy:HH:mm:ss:SS where d=day,M=month,y=year,H=hour,m=minute,s=secound,S=milisecound
     * Return NULL if something goes wrong
     * @since 1.0
     
    public static java.sql.Timestamp shortString2Timestamp(String str){
        try{
            // Parse the string back into a date.
            java.util.Date date = parseDate( str, STANDARD_DATE_FORMAT );
            //return the timestamp
            return new java.sql.Timestamp(date.getTime());
        }catch ( ParseException e){
            return null;
        }
    }*/

}
