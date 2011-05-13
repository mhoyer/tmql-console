TMQL Console
============

A console tool to quickly fire TMQL statements against local topic map files.

Similar GUI-based projects
--------------------------

* [Tamana](https://code.google.com/a/eclipselabs.org/p/tamana/)
* [Semagia Oomap Loomap](https://github.com/heuer/oomaploomap)

Sample use case
---------------

    $ ./tmql.sh pokemon.ltm
    TMQL4J Simple Client
    Importing pokemon.ltm ...done!

    >> Set current TMQL runtime to TMQL-2010.
    Enter '?' for help.
    TMQL-2010@pokemon.ltm > ?
    h, help, ?                   : Shows this screen.
    e, exit, q, quit             : Exits the console.
    s, stats                     : Shows the statistics for loaded Topic Map.
    t, tree                      : Prints the parsed tree for the last query.
    x, external <queryfile>      : Loads and executes content of queryfile.
    r, runtime <version>         : Changes the TMQL runtime.
                                     * TMQL-2010
                                     * TMQL-2007

    An entered query should be finalized with ; to execute it.

    TMQL-2010@pokemon.ltm > s
      * Topics: 201
      * Associations: 442

    TMQL-2010@pokemon.ltm > r TMQL-2007
    >> Set current TMQL runtime to TMQL-2007.
    TMQL-2007@pokemon.ltm > "poison" << atomify << characteristics;
    [EnteredQuery = "Poison" << atomify << characteristics]
    [ResultType = TMAPI]

    -  innerResults:
       -  construct: !Topic
             itemIdentifiers:
             - file:/D:/Temp/pokemon.ltm#poison
             names:
             -  itemIdentifiers: []
                scope: []
                type: http://psi.topicmaps.org/iso13250/model/topic-name
                value: Poison
                variants: []
             occurrences:
             -  datatype: http://www.w3.org/2001/XMLSchema#string
                itemIdentifiers: []
                scope: []
                type: ii:file:/D:/Temp/pokemon.ltm#description
                value: Poison Pokemon prefer to live in dumps and can poison other Pokemon.
             subjectIdentifiers: []
             subjectLocators: []


    [FoundResults = 1]
    TMQL-2007@pokemon.ltm > t
    Query tree for:
    "Poison" << atomify << characteristics
    QueryExpression(["Poison", <<, atomify, <<, characteristics])
      |--PathExpression(["Poison", <<, atomify, <<, characteristics])
        |--PostfixedExpression(["Poison", <<, atomify, <<, characteristics])
          |--SimpleContent(["Poison", <<, atomify, <<, characteristics])
            |--Anchor(["Poison"])
            |--Navigation([<<, atomify, <<, characteristics])
              |--StepDefinition([<<, atomify])
              | |--Step([<<, atomify])
              |--StepDefinition([<<, characteristics])
                |--Step([<<, characteristics])

    TMQL-2007@pokemon.ltm > e
    
    $



