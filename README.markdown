TMQL Console
============

A console tool to quickly fire TMQL statements against local topic map files.

    $ ./tmql.sh pokemon.ltm
    TMQL4J Simple Client
    Importing pokemon.ltm ...done!

    >> Set current TMQL runtime to TMQL-2010.
    Enter '?' for help.
    TMQL-2010@pokemon.ltm > ?
    ?
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

    TMQL-2010@pokemon.ltm >


Similar GUI-based projects:
* [Tamana](https://code.google.com/a/eclipselabs.org/p/tamana/)
* [Semagia Oomap Loomap](https://github.com/heuer/oomaploomap)

