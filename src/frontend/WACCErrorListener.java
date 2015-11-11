package frontend;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

public class WACCErrorListener implements ANTLRErrorListener {

    @Override
    public void reportAmbiguity(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3,
                    boolean arg4, @Nullable BitSet arg5,
                    @NotNull ATNConfigSet arg6) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reportAttemptingFullContext(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3,
                    @Nullable BitSet arg4, @NotNull ATNConfigSet arg5) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reportContextSensitivity(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3, int arg4,
                    @NotNull ATNConfigSet arg5) {
        // TODO Auto-generated method stub

    }

    @Override
    public void syntaxError(@NotNull Recognizer<?, ?> arg0,
                    @Nullable Object arg1, int line, int col,
                    @NotNull String errorMessage,
                    @Nullable RecognitionException arg5) {
        // System.out.println(arg0.toString());
        // System.out.println(arg1.toString());
        System.err.println("Syntactic error at " + line + ":" + col
                        + " -- " + errorMessage);
        // System.out.println(arg2 + " " + arg3); // Position of the error
        // System.out.println(arg4.toString()); // Some default error message
        // System.out.println(arg5.toString()); // Type of exception
    }

}
