package de.topicmapslab.tmql4j;

import java.io.File;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 11:47:58
 */
public class SimpleClient {
    public static void main(String[] args)
    {
        puts("TMQL4J Simple Client");

        if (args.length == 0) {
            puts("Invalid arguments count (%d).", args.length);
            showHelp();
            return;
        }

        File topicMapFile = new File(args[0]);
        if (!topicMapFile.exists()) {
            puts("File not found:\n\n%s", args[0]);
            return;
        }

        try {
            QueryConsole console = new QueryConsole(topicMapFile);
            console.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showHelp()
    {
        puts("Usage: SimpleClient <Path-to-TopicMap-file> <TMQL Query>");
    }

    public static void puts(String message, Object... args)
    {
        System.out.println(String.format(message, args));
    }

    public static void put(String message, Object... args)
    {
        System.out.print(String.format(message, args));
    }
}
