package com.clouds42;

import com.github._1c_syntax.bsl.parser.BSLLexer;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

import java.util.Set;

public class LinesToCoverage {

    private static final Set<Class<? extends BSLParserRuleContext>> contexts = Set.of(
            BSLParser.AssignmentContext.class,
            BSLParser.CallStatementContext.class,
            BSLParser.GotoStatementContext.class,
            BSLParser.ReturnStatementContext.class,
            BSLParser.BreakStatementContext.class,
            BSLParser.ContinueStatementContext.class,
            BSLParser.IfStatementContext.class,
            BSLParser.RaiseStatementContext.class,
            BSLParser.ForEachStatementContext.class,
            BSLParser.ForStatementContext.class,
            BSLParser.WhileStatementContext.class,
            BSLParser.GlobalMethodCallContext.class
    );

    private static final Set<Integer> tokenTypes = Set.of(
            BSLLexer.ENDDO_KEYWORD,
            BSLLexer.ENDFUNCTION_KEYWORD,
            BSLLexer.ENDPROCEDURE_KEYWORD,
            BSLLexer.ENDTRY_KEYWORD,
            BSLLexer.ENDIF_KEYWORD,
            BSLLexer.DO_KEYWORD
    );

    static int[] getLines(BSLParserRuleContext ast) {

        return Trees.getDescendants(ast).stream()
                .filter(LinesToCoverage::mustCovered)
                .mapToInt(LinesToCoverage::getLine)
                .distinct().toArray();
    }

    static boolean mustCovered(ParseTree node) {

        if (node instanceof ParserRuleContext) {
            return contexts.contains(node.getClass());
        } else if (node instanceof TerminalNode) {
            return tokenTypes.contains(
                    ((TerminalNode) node).getSymbol().getType()
            );
        }

        throw new IllegalArgumentException();
    }

    private static int getLine(ParseTree node) {

        if (node instanceof ParserRuleContext) {
            return ((ParserRuleContext) node).getStart().getLine();
        } else if (node instanceof TerminalNode) {
            return ((TerminalNode) node).getSymbol().getLine();
        }

        throw new IllegalArgumentException();
    }
}
