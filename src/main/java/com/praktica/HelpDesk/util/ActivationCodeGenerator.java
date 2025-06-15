package com.praktica.HelpDesk.util;

import java.util.concurrent.ThreadLocalRandom;

public class ActivationCodeGenerator {
    public static String generateCode(){
        int code = ThreadLocalRandom.current().nextInt(0, 1000000);
        return String.format("%06d", code);
    }
}
