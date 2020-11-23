package com.github.ybroeker.pmdidea.pmd;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public interface ScannableFile {

    String getDisplayName();

    InputStream getInputStream();

}
