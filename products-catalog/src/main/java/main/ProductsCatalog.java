package main;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;

import java.util.logging.Logger;

public class ProductsCatalog /*implements InitializingBean, DisposableBean*/ {

    @GigaSpaceContext
    private GigaSpace gigaSpace;
}