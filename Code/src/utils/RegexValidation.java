package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidation {

    public static boolean validateRoomNum(String roomNum) {
        Pattern pattern = Pattern.compile("[1-9][0-9][0-9]");
        Matcher matcher = pattern.matcher(roomNum.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }
    public static boolean validateName(String name) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]{1,49}");
        Matcher matcher = pattern.matcher(name.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateSurname(String roomSurname) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]{1,49}");
        Matcher matcher = pattern.matcher(roomSurname.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateEmail(String roomEmail) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
        Matcher matcher = pattern.matcher(roomEmail.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateUIDAI(String roomUIDAI) {
        Pattern pattern = Pattern.compile("[0-9]{12}");
        Matcher matcher = pattern.matcher(roomUIDAI.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validatePhone(String roomPhone) {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(roomPhone.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validatePassport(String roomPassport) {
        Pattern pattern = Pattern.compile("[A-Z_0-9]{1,50}");
        Matcher matcher = pattern.matcher(roomPassport.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateProfession(String roomNum) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]{1,49}");
        Matcher matcher = pattern.matcher(roomNum.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateMaritalStatus(String roomMaritalStatus) {
        if(roomMaritalStatus.equals("married"))
            return true;
        else
            return false;
    }

    public static boolean validateArrival(String roomArrival) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
        Matcher matcher = pattern.matcher(roomArrival.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateDeparture(String roomDeparture) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
        Matcher matcher = pattern.matcher(roomDeparture.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateAddress(String roomAddress) {
        Pattern pattern = Pattern.compile("[a-zA-Z]{1,300}");
        Matcher matcher = pattern.matcher(roomAddress.trim());
        boolean matchFound = matcher.find();
        if(matchFound)
        {
            return true;
        }
        return false;
    }

    public static boolean validateOccupants(String roomOccupants) {
        if(Integer.valueOf(roomOccupants)>=1 && Integer.valueOf(roomOccupants)<=6)
            return true;
        else
            return false;
    }



}
