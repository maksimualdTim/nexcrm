package uz.nexgroup.nexcrm.component;

import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;

public class DomainUtils {
    @Value("${domain}")
    private static String mainDomain;

    private static final Pattern NON_LATIN = Pattern.compile("[^A-Za-z0-9-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    
    public static String generateDomain(String username) {
        // Normalize the username
        String normalized = Normalizer.normalize(username, Normalizer.Form.NFD);
        
        // Replace non-Latin characters
        String domainPart = NON_LATIN.matcher(normalized).replaceAll("").trim();
        
        // Replace spaces with hyphens
        domainPart = WHITESPACE.matcher(domainPart).replaceAll("-");
        
        // Ensure the domain is lowercase
        domainPart = domainPart.toLowerCase();

        // Limit the length of the domain (example: maximum 63 characters for the domain part)
        if (domainPart.length() > 63) {
            domainPart = domainPart.substring(0, 63);
        }
        
        // Example domain suffix
        return domainPart + "." + mainDomain; // Replace with your actual domain suffix
    }
}

