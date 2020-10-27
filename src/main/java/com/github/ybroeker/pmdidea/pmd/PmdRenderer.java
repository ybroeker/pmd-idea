package com.github.ybroeker.pmdidea.pmd;

import java.io.IOException;
import java.util.Iterator;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;
import net.sourceforge.pmd.util.datasource.DataSource;
import org.jetbrains.annotations.NotNull;

public class PmdRenderer extends AbstractIncrementingRenderer {

    private final PmdRunListener pmdRunListener;

    public PmdRenderer(final PmdRunListener pmdRunListener) {
        super("pmd-idea", "PMD plugin renderer");
        this.pmdRunListener = pmdRunListener;
        setWriter(new NullWriter());
    }

    @Override
    public String defaultFileExtension() {
        return "txt";
    }

    @Override
    public void start() {
    }

    @Override
    public void end() {
        pmdRunListener.finished();
    }

    @Override
    public void startFileAnalysis(final DataSource dataSource) {
        super.startFileAnalysis(dataSource);
        pmdRunListener.processFile();
    }

    @Override
    public void renderFileViolations(final Iterator<RuleViolation> violations) {
        for (final RuleViolation ruleViolation : new IteratorIterable<>(violations)) {
            pmdRunListener.addViolation(ruleViolation);
        }
    }

    private static class IteratorIterable<T> implements Iterable<T> {

        private final Iterator<T> wrappedIterator;

        public IteratorIterable(final Iterator<T> wrappedIterator) {
            this.wrappedIterator = wrappedIterator;
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            return wrappedIterator;
        }
    }

}
