package de.topicmapslab.tmql4j;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;
import de.topicmapslab.majortom.remoting.contracts.dto.tmdm.*;
import de.topicmapslab.majortom.remoting.converter.dto.tmdm.QueryConverter;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;

import java.io.PrintStream;
import java.io.StringWriter;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 22:46:15
 */
public class TMQLResultYTMWriter {
    private PrintStream output;
    private QueryConverter queryConverter;
    private YamlWriter yamlWriter;
    private StringWriter yamlOutput;

    public TMQLResultYTMWriter(PrintStream outputStream) {
        output = outputStream;
        queryConverter = new QueryConverter();
    }

    public void write(IResultSet<?> resultSet)
    {
        QueryResultDTO[] result = queryConverter.convert(resultSet);

        try {
            initYamlWriter();
            yamlWriter.write(result);
            yamlWriter.close();
            output.println(yamlOutput.toString());
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    public void write(ConstructDTO construct)
    {
        try {
            initYamlWriter();
            yamlWriter.write(construct);
            yamlWriter.close();
            output.println(yamlOutput.toString());
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        if (yamlWriter != null) try {
            yamlWriter.close();
        } catch (YamlException e) {
            e.printStackTrace();
        }
    }

    private void initYamlWriter() {
        yamlOutput = new StringWriter();
        yamlWriter = new YamlWriter(yamlOutput);

        YamlConfig yamlConfig = yamlWriter.getConfig();
        yamlConfig.setClassTag("TopicMap", TopicMapDTO.class);
        yamlConfig.setClassTag("Topic", TopicDTO.class);
        yamlConfig.setClassTag("Occurrence", OccurrenceDTO.class);
        yamlConfig.setClassTag("Name", NameDTO.class);
        yamlConfig.setClassTag("Variant", VariantDTO.class);
        yamlConfig.setClassTag("Association", AssociationDTO.class);
        yamlConfig.setClassTag("Role", RoleDTO.class);
    }
}
