package de.topicmapslab.tmql4j;

import de.topicmapslab.tmql4j.common.model.query.IQuery;
import de.topicmapslab.tmql4j.resultprocessing.core.ctm.CTMResult;
import de.topicmapslab.tmql4j.resultprocessing.core.xml.XMLResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import de.topicmapslab.tmql4j.resultprocessing.model.ResultType;

import java.io.PrintStream;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 12:38:55
 */
public class ResultInterpreter {
    private PrintStream outputStream;

    public ResultInterpreter(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void printResults(IQuery query) {
        String resultType = query.getResults().getResultType();

        outputStream.println(String.format("[ResultType = %s]\n", resultType));

        if (resultType.equalsIgnoreCase(ResultType.XML.toString())) {
            outputStream.println(XMLResult.class.cast(query.getResults()).resultsAsMergedXML());
            return;
        }

        if (resultType.equalsIgnoreCase(ResultType.CTM.toString())) {
            outputStream.println(CTMResult.class.cast(query.getResults()).resultsAsMergedCTM());
            return;
        }

        printComplexResults(query);
    }

    private void printComplexResults(IQuery query) {
        IResultSet<?> set = query.getResults();
        TMQLResultYTMWriter ytmWriter = new TMQLResultYTMWriter(outputStream);
        ytmWriter.write(set);

        outputStream.println();
        outputStream.println(String.format("[FoundResults = %d]", set.size()));
    }
}
