package de.topicmapslab.tmql4j;

import de.topicmapslab.tmql4j.common.context.TMQLRuntimeProperties;
import de.topicmapslab.tmql4j.common.core.runtime.TMQLRuntimeFactory;
import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.common.model.runtime.ITMQLRuntime;
import org.tmapi.core.TMAPIException;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapix.io.CTMTopicMapReader;
import org.tmapix.io.LTMTopicMapReader;
import org.tmapix.io.TopicMapReader;
import org.tmapix.io.XTMTopicMapReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 12:07:01
 */
public class QueryConsole {
    private String prefix;
    private TopicMap topicMap;
    private ITMQLRuntime runtime;
    private ResultInterpreter resultInterpreter = new ResultInterpreter(System.out);
    private TopicMapSystem topicMapSystem;

    public QueryConsole(File topicMapFile) throws TMAPIException, IOException {
        prefix = topicMapFile.getName();

        importTopicMap(topicMapFile);

        runtime = TMQLRuntimeFactory.newFactory().newRuntime(topicMapSystem, topicMap);
        runtime.setEnvironmentMap(topicMapSystem.createTopicMap("http://foo.de/env"));
        TMQLRuntimeProperties props = runtime.getProperties();
        //props.enableMaterializeMetaModel(true);
        props.put(TMQLRuntimeProperties.MATERIALIZE_META_MODEL, false);
    }

    private void importTopicMap(File topicMapFile) throws TMAPIException, IOException {
        SimpleClient.put("Importing %s ...", topicMapFile.getName());
        String fileName = topicMapFile.getName().toLowerCase();

        topicMapSystem = TopicMapSystemFactory.newInstance()
                .newTopicMapSystem();
        topicMap = topicMapSystem
                .createTopicMap(topicMapFile.toURI().toString());

        TopicMapReader tmReader;
        if(fileName.endsWith(".ltm")) {
            tmReader = new LTMTopicMapReader(topicMap, topicMapFile);
        }
        else if(fileName.endsWith(".ctm")) {
            tmReader = new CTMTopicMapReader(topicMap, topicMapFile);
        }
        else tmReader = new XTMTopicMapReader(topicMap, topicMapFile);

        tmReader.read();
        SimpleClient.puts("done!\n  * Topics: %d\n  * Associations: %d\n", topicMap.getTopics().size(), topicMap.getAssociations().size());
    }

    public void open() throws IOException {
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        String q = "";
        while(true)
        {
            SimpleClient.put("%s %s ", prefix, q.isEmpty() ? ">" : "|");
            
            String line = in.readLine();
            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) break;

            q = q.concat(line);
            if (!line.trim().endsWith(";")) continue;

            q = q.substring(0, q.lastIndexOf(";")+1);
            runQuery(q);

            q = "";
        }
    }

    public void runQuery(String q)
    {
        try {
            IQuery query = runtime.run(q);
            resultInterpreter.printResults(query);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }

    }

}
