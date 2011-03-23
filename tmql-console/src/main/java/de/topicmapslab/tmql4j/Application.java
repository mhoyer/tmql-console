package de.topicmapslab.tmql4j;

import de.topicmapslab.tmql4j.draft2010.components.processor.runtime.TmqlRuntime;
import de.topicmapslab.tmql4j.path.components.processor.runtime.TmqlRuntime2007;

import java.io.File;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 11:47:58
 */
public class Application {
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
            QueryConsole console = new QueryConsole(System.out, topicMapFile);
            console.registerRuntime(TmqlRuntime.TMQL_2010);
            console.registerRuntime(TmqlRuntime2007.TMQL_2007);

            String initialRuntime = System.getenv("TMQL_CONSOLE_RUNTIME");
            if (initialRuntime == null || initialRuntime.trim().length() == 0) initialRuntime = TmqlRuntime.TMQL_2010;
            if (!console.toggleRuntime(initialRuntime)) return;

            if (args.length > 1) {
                String query = "";
                for(int i=1; i < args.length; i++)
                {
                    query = query.concat(args[i]) + " ";
                }
                
                console.runQuery(query);
            }
            else {
                console.open();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showHelp()
    {
        puts("Usage: TMQLConsole <Path-to-TopicMap-file> [<TMQL Query>]");
    }

    public static void puts(String message, Object... args)
    {
        System.out.println(String.format(message, args));
    }
}
