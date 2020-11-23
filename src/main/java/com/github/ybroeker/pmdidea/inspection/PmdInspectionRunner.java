package com.github.ybroeker.pmdidea.inspection;

import com.intellij.codeInspection.InspectionManager;


public class PmdInspectionRunner {

    PsiElements wrappedFile;

    InspectionManager inspectionManager;

    public PmdInspectionRunner(final PsiElements wrappedFile, final InspectionManager inspectionManager) {
        this.wrappedFile = wrappedFile;
        this.inspectionManager = inspectionManager;
    }
}
