package frontend;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

public class WACCErrorListener implements ANTLRErrorListener {
    private List<String> errorMessages = new ArrayList<String>();

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    @Override
    public void reportAmbiguity(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3,
                    boolean arg4, @Nullable BitSet arg5,
                    @NotNull ATNConfigSet arg6) {
    }

    @Override
    public void reportAttemptingFullContext(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3,
                    @Nullable BitSet arg4, @NotNull ATNConfigSet arg5) {
    }

    @Override
    public void reportContextSensitivity(@NotNull Parser arg0,
                    @NotNull DFA arg1, int arg2, int arg3, int arg4,
                    @NotNull ATNConfigSet arg5) {
    }

    @Override
    public void syntaxError(@NotNull Recognizer<?, ?> arg0,
                    @Nullable Object arg1, int line, int col,
                    @NotNull String errorMessage,
                    @Nullable RecognitionException arg5) {
        errorMessages.add("Syntactic error at " + line + ":" + col
                        + " -- " + errorMessage);
    }

}
