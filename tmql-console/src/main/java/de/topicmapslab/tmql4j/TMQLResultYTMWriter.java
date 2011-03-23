package de.topicmapslab.tmql4j;

import de.topicmapslab.tmql4j.common.core.exception.TMQLRuntimeException;
import de.topicmapslab.tmql4j.common.utility.XmlSchemeDatatypes;
import de.topicmapslab.tmql4j.resultprocessing.core.reduction.ReductionResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.reduction.ReductionTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleResultSet;
import de.topicmapslab.tmql4j.resultprocessing.core.simple.SimpleTupleResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResult;
import de.topicmapslab.tmql4j.resultprocessing.model.IResultSet;
import org.tmapi.core.*;

import java.io.PrintStream;
import java.util.Set;

/**
 * User: mhoyer
 * Date: 08.09.2010
 * Time: 22:46:15
 */
public class TMQLResultYTMWriter {
    private PrintStream output;
    private int indention;
    private String indent = "";
    private boolean isInLine = true;

    public TMQLResultYTMWriter(PrintStream outputStream) {
        this.output = outputStream;
    }

    public void write(IResultSet<?> resultSet)
    {
        indention = 0;

        Class<? extends IResult> resultType = resultSet.getResultClass();
        if (ReductionTupleResult.class.isAssignableFrom(resultType)) {
            writeResultSet(ReductionResultSet.class.cast(resultSet));
        }
        else if (SimpleTupleResult.class.isAssignableFrom(resultType)) {
            writeResultSet(SimpleResultSet.class.cast(resultSet));
        }
        else { System.err.println(String.format("%s is not supported", resultType.getName())); }
    }

    private void writeResultSet(SimpleResultSet simpleResultSet) {
        for(SimpleTupleResult tuple : simpleResultSet.getResults())
        {
            writeSimpleTupleResult(tuple);
        }
    }

    private void writeSimpleTupleResult(SimpleTupleResult results)
    {
        put("- ");

        indent();
        for(Object result : results.getResults())
        {
            if (result instanceof Topic) {
                writeTopic((Topic) result);
            } else if (result instanceof Variant) {
                writeVariant((Variant) result);
            } else if (result instanceof Name) {
                writeName((Name) result);
            } else if (result instanceof Association) {
                writeAssociation((Association) result);
            } else if (result instanceof Occurrence) {
                writeOccurrence((Occurrence) result);
            } else if (result instanceof String) {
                writeString((String) result);
            } else if (result instanceof Locator) {
                writeLocator((Locator) result);
            } else if (result instanceof SimpleTupleResult) {
                puts("set:");
                indent();
                writeSimpleTupleResult((SimpleTupleResult) result);
                unindent();
            } else {
                System.err.println("Missing writer for " + result.getClass().getName());
            }
        }
        unindent();
    }

    private void writeTopic(Topic topic) {
        String ref = topicRef(topic);
        puts("topic       : %s", ref);
    }

    private void writeName(Name name) {
        puts("name        : { value: \"%s\", type: <%s> }", name.getValue(), topicRef(name.getType()));

        indent();
        {
            writeScope(name);
        }
        unindent();
    }

    private void writeScope(Scoped scoped) {
        if (scoped.getScope().size() == 0) return;
        String themes = "";
        for(Topic theme : scoped.getScope())
        {
            if (!themes.isEmpty()) themes = ", ".concat(themes);
            themes.concat(topicRef(theme));
        }
        puts("scope: [%s]", themes);
    }

    private void writeAssociation(Association association) {
        puts("association : %s", topicRef(association.getType()));
    }

    private void writeOccurrence(Occurrence occurrence) {
        puts("occurrence  :");

        indent();
        {
            String datatype = occurrence.getDatatype().getReference();

            if (datatype.equals(XmlSchemeDatatypes.XSD_STRING) ||
                    datatype.equals(XmlSchemeDatatypes.XSD_ANY) ||
                    datatype.equals(XmlSchemeDatatypes.XSD_ANYURI) ||
                    datatype.equals(XmlSchemeDatatypes.XSD_QSTRING) ||
                    datatype.equals(XmlSchemeDatatypes.XSD_QANY) ||
                    datatype.equals(XmlSchemeDatatypes.XSD_QANYURI)) {

                puts("value       : \"%s\"", occurrence.getValue());
            } else {
                puts("value       : %s [%s]", occurrence.getValue());
            }

            puts("type        : %s", topicRef(occurrence.getType()));
            puts("datatype    : %s", datatype.replaceAll("http://www\\.w3\\.org/2001/XMLSchema#", ""));
        }
        unindent();
    }

    private void writeString(String value) {
        puts("\"%s\"", value);
    }

    private void writeLocator(Locator locator) {
        puts(locator.getReference());
    }

    private void writeVariant(Variant variant) {
        puts("variant     : \"%s\"", variant.getValue());
    }

    private void puts(String value, Object... args) {
        output.println(withIndention(value, args));
        isInLine = false;
    }

    private void put(String value, Object... args) {
        output.print(withIndention(String.format(value, args)));
        isInLine = true;
    }

    private String withIndention(String value, Object... args) {
        if(isInLine) {
            return String.format(value, args);
        }

        return String.format(indent.concat(value), args);
    }

    private void indent() {
        indent = indent.concat("  ");
        indention++;
    }
    private void unindent() {
        indention--;
        if (!indent.isEmpty()) indent = indent.substring(2);
    }

    /**
	 * Returns an IRI which is usable to as reference to the specified topic.
	 *
	 * Modified version of JTMTopicMapWriter#_topicRef from Lars Heuer
	 * (heuer[at]semagia.com)
	 *
	 * @param topic
	 *            The topic.
	 * @return An IRI.
	 */
	private String topicRef(Topic topic) {
		Set<Locator> locs = topic.getSubjectIdentifiers();
		if (!locs.isEmpty()) {
			return "si:" + locs.iterator().next().toExternalForm();
		}

		locs = topic.getSubjectLocators();
		if (!locs.isEmpty()) {
			return "sl:" + locs.iterator().next().toExternalForm();
		}

        locs = topic.getItemIdentifiers();
        if (locs.isEmpty())
            throw new TMQLRuntimeException("Topic " + topic + " has no SI nor SL nor an II, thus violates the TMDM");

        return "ii:" + locs.iterator().next().toExternalForm();
	}
}
