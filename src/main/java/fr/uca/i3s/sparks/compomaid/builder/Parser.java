package fr.uca.i3s.sparks.compomaid.builder;


import fr.uca.i3s.sparks.MeLaLexer;
import fr.uca.i3s.sparks.MeLaParser;
import fr.uca.i3s.sparks.compomaid.model.App.App;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Parser {

    public static App parseMeLa (String app_str) {

        // Parse MeLa language
        MeLaLexer mela_lexer = new MeLaLexer(new ANTLRInputStream(app_str));
        MeLaParser mela_parser = new MeLaParser(new CommonTokenStream(mela_lexer));

        // Create builder object
        MeLaBuilder builder = new MeLaBuilder();

        // Walk through the MeLa language to build the app
        ParseTreeWalker walker  = new ParseTreeWalker();
        walker.walk(builder, mela_parser.app());

        // Retreive model
        return builder.retrieve();
    }
}
