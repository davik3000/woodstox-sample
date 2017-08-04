/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dummy.woodstox.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author davik3000
 */
public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public Main() {
    }

    public void execute(String[] args) {
        if (args.length > 0) {
            String filePath = args[0];

            Processor p = new Processor();
            p.testEventReaderWriter(filePath, "http://dummy.org/2.0", "tag2", filePath + ".output.xml");
        }
    }

    public static void main(String[] args) {
        LOG.debug("Starting application");
        Main m = new Main();
        m.execute(args);
        LOG.debug("Ending application");
    }
}
