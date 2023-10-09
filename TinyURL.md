## Problem Statement

Design a Tiny URL service that converts long URL to short URL and vice versa.

* e.g. Long URL : `"http://wwww.northeastern.edu"` . Equivalent Short URL : `http://bit.ly/zGjvTmP`

## Solution

I have assumed my code generates short URL of length 7 everytime. Below is the complete code in `Java`.

```Java
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TinyURL {

    private static Map<String, String> m = new HashMap<>();
    private static String c = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static Random rand = new Random();
    private static String key = getNewKey();  // assigning default value to first URL

    public static String encode(String longURL) {
        while(m.containsKey(key)) {  // In real life - checking database for duplicate key
            key = getNewKey();
        }
        m.put(key, longURL);
        return "http://bit.ly/"+key;
    }

    private static String getNewKey() {
        StringBuilder newKey = new StringBuilder();

        // Fixing the length of my shortURL service to 7
        for (int i=0;i<7;i++) {
            newKey.append(c.charAt(rand.nextInt(62)));
        }
        return newKey.toString();
    }

    private static String decode(String shortURL) {
        return m.get(shortURL.replace("http://bit.ly/",""));  // this will be replaced with Database query in real life
    }

    public static void main(String[] args) {
        String test1 = "http://wwww.northeastern.edu";
        System.out.println("Encoded String : "+encode(test1));
        System.out.println("Decoded String : "+decode(encode(test1)));
    }
}

```


Thanks.
